package com.alienlabz.packagez.model;

import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.database.Database;
import com.alienlabz.packagez.service.Synchronizer;
import com.alienlabz.packagez.util.CursorList;
import com.alienlabz.packagez.util.Strings;

/**
 * Domain Class for Packages.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
final public class Pack extends Model<Pack> {
	public static final String DELIVERED = "delivered";
	public String lastDate;
	public String code;
	public Status status;
	public String description;
	public Carrier carrier;
	public String firstDate;
	public boolean delivered;
	public Category category;

	@Override
	public boolean equals(Object o) {
		Pack pack = (Pack) o;
		return pack != null && pack.code != null && pack.code.equals(code);
	}

	/**
	 * Find all packages.
	 * 
	 * @param context Context.
	 * @return List of Packages.
	 */
	public static CursorList<Pack> findAll() {
		final Cursor cursor = Database
				.getInstance()
				.getReadableDatabase()
				.rawQuery(
						"select * from " + Database.Package.TABLE_NAME + " where " + Database.Package.DELETED + "=0"
								+ " and " + Database.Package.CATEGORY_ID + "!=(select " + Database.Category.ID
								+ " from " + Database.Category.TABLE_NAME + " where " + Database.Category.NAME + "=?)",
						new String[] { Database.context.getResources().getString(R.string.category_archived) });
		return new CursorList<Pack>(cursor, Pack.class);
	}

	/**
	 * Find packages by status.
	 * 
	 * @param context Context.
	 * @param status Status.
	 * @return List of Packages.
	 */
	public static CursorList<Pack> findByStatus(final Status status) {
		final Cursor cursor = Database
				.getInstance()
				.getReadableDatabase()
				.rawQuery(
						"select * from " + Database.Package.TABLE_NAME + " where " + Database.Package.LAST_STATUS
								+ " = ? " + " and " + Database.Package.DELETED + "=0",
						new String[] { status.toString() });
		return new CursorList<Pack>(cursor, Pack.class);
	}

	/**
	 * Insert a New Package.
	 * 
	 * @param context Context.
	 */
	public void insert() {
		if (category.id == 0) {
			category.insert();
		}
		ContentValues values = toContentValues();
		values.put(Database.Package.SYNC_FLAG, Synchronizer.SYNC_INSERT);
		id = Database.getInstance().getWritableDatabase().insertOrThrow(Database.Package.TABLE_NAME, null, values);
	}

	public static List<Pack> findDeletions() {
		final Cursor cursor = Database
				.getInstance()
				.getReadableDatabase()
				.rawQuery(
						"select * from " + Database.Package.TABLE_NAME + " where " + Database.Package.SYNC_FLAG
								+ " = ?", new String[] { Synchronizer.SYNC_DELETE });
		return new CursorList<Pack>(cursor, Pack.class);
	}

	public static List<Pack> findInsertions() {
		final Cursor cursor = Database
				.getInstance()
				.getReadableDatabase()
				.rawQuery(
						"select * from " + Database.Package.TABLE_NAME + " where " + Database.Package.SYNC_FLAG
								+ " = ?", new String[] { Synchronizer.SYNC_INSERT });
		return new CursorList<Pack>(cursor, Pack.class);

	}

	/**
	 * Delete a Package.
	 * 
	 * @param context Package.
	 * @param code Package Code.
	 */
	public static void forceDelete() {
		final Cursor cursor = Database
				.getInstance()
				.getReadableDatabase()
				.rawQuery("select * from " + Database.Package.TABLE_NAME + " where " + Database.Package.DELETED + "=1",
						null);

		while (cursor.moveToNext()) {
			Track.deleteByCode(cursor.getString(cursor.getColumnIndex(Database.Package.CODE)));
			Long catId = cursor.getLong(cursor.getColumnIndex(Database.Package.CATEGORY_ID));
			Category category = Category.findById(catId);
			if (category.custom && countByCategory(category) <= 0) {
				category.delete();
			}
		}

		cursor.close();

		ContentValues values = new ContentValues();
		values.put(Database.Package.SYNC_FLAG, Synchronizer.SYNC_DELETE);
		Database.getInstance().getWritableDatabase()
				.update(Database.Package.TABLE_NAME, values, Database.Package.DELETED + "=1", null);
	}

	public static void undelete() {
		final ContentValues values = new ContentValues();
		values.put(Database.Package.DELETED, 0);
		Database.getInstance().getWritableDatabase().update(Database.Package.TABLE_NAME, values, null, null);
	}

	/**
	 * Delete a Package.
	 * 
	 * @param context Package.
	 * @param code Package Code.
	 */
	public static void delete(final String code) {
		final ContentValues values = new ContentValues();

		values.put(Database.Package.DELETED, 1);

		Database.getInstance()
				.getWritableDatabase()
				.update(Database.Package.TABLE_NAME, values, Database.Package.CODE + "=?",
						new String[] { code.toUpperCase(Locale.ENGLISH) });

	}

	/**
	 * Update this Package.
	 * 
	 * @param context Context.
	 */
	public void update() {
		final ContentValues values = new ContentValues();

		if (status != null) {
			values.put(Database.Package.LAST_STATUS, status.toString());
		}

		values.put(Database.Package.DELIVERED, delivered);
		values.put(Database.Package.LAST_DATE, lastDate);
		values.put(Database.Package.FIRST_DATE, firstDate);

		if (category != null) {
			values.put(Database.Package.CATEGORY_ID, category.id);
		}

		Database.getInstance()
				.getWritableDatabase()
				.update(Database.Package.TABLE_NAME, values, Database.Package.CODE + "=?",
						new String[] { code.toUpperCase(Locale.ENGLISH) });
	}

	/**
	 * Find all "Non-Delivered" Packages.
	 * 
	 * @param context Context.
	 * @return List of Packages.
	 */
	public static CursorList<Pack> findNotDeliveredPackages() {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return new CursorList<Pack>(db.rawQuery("select * from " + Database.Package.TABLE_NAME + " where "
				+ Database.Package.DELIVERED + "=?" + " and " + Database.Package.DELETED + "=0", new String[] { "0" }),
				Pack.class);
	}

	/**
	 * Find Packages based on a Query String.
	 * 
	 * @param context Context.
	 * @param query Query String.
	 * @return List of Packages.
	 */
	public static CursorList<Pack> findPackages(final String query) {
		CursorList<Pack> result = null;

		if (Strings.isEmpty(query)) {
			result = findAll();
		} else {
			final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
			result = new CursorList<Pack>(db.rawQuery("select * from " + Database.Package.TABLE_NAME + " where "
					+ Database.Package.CODE + " like ? or " + Database.Package.DESCRIPTION + " like ? " + " and "
					+ Database.Package.DELETED + "=0", new String[] { "%" + query + "%", "%" + query + "%" }),
					Pack.class);
		}

		return result;
	}

	/**
	 * Find Packages by Category.
	 * 
	 * @param context Context.
	 * @param category Category.
	 * @return List of Packages.
	 */
	public static CursorList<Pack> findByCategory(final Category category) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();

		final String defaultCategory = Database.context.getResources().getString(R.string.default_category);
		Cursor cursor = null;
		if (defaultCategory.equals(category.name)) {

			cursor = db.rawQuery("select * from " + Database.Package.TABLE_NAME + " where " + Database.Package.DELETED
					+ "=0" + " and " + Database.Package.CATEGORY_ID + "!=(select " + Database.Category.ID + " from "
					+ Database.Category.TABLE_NAME + " where " + Database.Category.NAME + "=?)",
					new String[] { Database.context.getResources().getString(R.string.category_archived) });

		} else {
			cursor = db.rawQuery("select * from " + Database.Package.TABLE_NAME + " where "
					+ Database.Package.CATEGORY_ID + " = ?" + " and " + Database.Package.DELETED + "=0",
					new String[] { String.valueOf(category.id) });
		}

		return new CursorList<Pack>(cursor, Pack.class);
	}

	/**
	 * Count total delivered packages.
	 * 
	 * @param context Context.
	 * @return Count.
	 */
	public static long countByStatus(final Status status) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, Database.Package.TABLE_NAME, Database.Package.LAST_STATUS
				+ " = ? and " + Database.Package.DELETED + "=0", new String[] { status.toString() });
	}

	public static long countByCode(Context context, final String code) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, Database.Package.TABLE_NAME, Database.Package.CODE + "=?" + " and "
				+ Database.Package.DELETED + "=0",
				new String[] { code.toUpperCase(context.getResources().getConfiguration().locale) });
	}

	/**
	 * Count total incoming packages.
	 * 
	 * @param context Context.
	 * @return Count.
	 */
	public static long countByCategory(final Category category) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, Database.Package.TABLE_NAME, Database.Package.CATEGORY_ID + "=?"
				+ " and " + Database.Package.DELETED + "=0", new String[] { String.valueOf(category.id) });
	}

	public static long countTotalPackages() {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, Database.Package.TABLE_NAME, Database.Package.DELETED + "=0", null);
	}

	/**
	 * Package Category.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */

	@Override
	public void fromCursor(Cursor cursor) {
		id = cursor.getLong(cursor.getColumnIndex(Database.Package.ID));
		code = cursor.getString(cursor.getColumnIndex(Database.Package.CODE));
		lastDate = cursor.getString(cursor.getColumnIndex(Database.Package.LAST_DATE));
		delivered = cursor.getInt((cursor.getColumnIndex(Database.Package.DELIVERED))) == 1 ? true : false;
		category = Category.findById(cursor.getLong(cursor.getColumnIndex(Database.Package.CATEGORY_ID)));
		status = Status.fromString(cursor.getString(cursor.getColumnIndex(Database.Package.LAST_STATUS)));
		firstDate = cursor.getString((cursor.getColumnIndex(Database.Package.FIRST_DATE)));
		description = cursor.getString(cursor.getColumnIndex(Database.Package.DESCRIPTION));
		carrier = Carrier.findById(cursor.getLong(cursor.getColumnIndex(Database.Package.CARRIER_ID)));
	}

	@Override
	public void fromJSON(JSONObject jsonObject) {
		try {
			id = jsonObject.getLong("id");
			code = jsonObject.getString("code");
			delivered = jsonObject.getBoolean("delivered");
			description = jsonObject.getString("description");
			category = new Category();
			category.fromJSON(jsonObject.getJSONObject("category"));
			carrier = new Carrier();
			carrier.fromJSON(jsonObject.getJSONObject("carrier"));
		} catch (JSONException e) {
		}
	}

	@Override
	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();

		values.put(Database.Package.CODE, code.toUpperCase(Locale.ENGLISH));
		values.put(Database.Package.DESCRIPTION, description);
		values.put(Database.Package.CARRIER_ID, carrier.id);
		values.put(Database.Package.FIRST_DATE, firstDate);
		values.put(Database.Package.DELETED, 0);
		values.put(Database.Package.CATEGORY_ID, category.id);
		values.put(Database.Package.DELIVERED, delivered);
		values.put(Database.Package.LAST_STATUS, status != null ? status.toString() : "");

		return values;
	}

}
