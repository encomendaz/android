package com.alienlabz.packagez.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.database.Database;
import com.alienlabz.packagez.util.CursorList;

public class Category extends Model<Category> {
	public static final long CATEGORY_ARCHIVED = 5;
	public String name;
	public boolean custom;

	public Category() {
	}

	public Category(final String name) {
		this.name = name;
	}

	public void insert() {
		final SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		id = db.insert(Database.Category.TABLE_NAME, null, toContentValues());
	}

	/**
	 * Find all categories with a specific name.
	 * 
	 * @param context Context.
	 * @param name Name.
	 * @return List of Categories.
	 */
	public static CursorList<Category> findByName(final String name) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();

		final Cursor cursor = db.rawQuery("select  * from " + Database.Category.TABLE_NAME + " where "
				+ Database.Category.NAME + " like ?", new String[] { "%" + name + "%" });

		return new CursorList<Category>(cursor, Category.class);
	}

	/**
	 * Find all categories.
	 * 
	 * @param context Context.
	 * @return List of Categories.
	 */
	public static CursorList<Category> findAllNoDefault(final Context context) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return new CursorList<Category>(db.rawQuery("select *  from " + Database.Category.TABLE_NAME + " where "
				+ Database.Category.NAME + "!=?",
				new String[] { context.getResources().getString(R.string.default_category) }), Category.class);
	}

	/**
	 * Find all categories.
	 * 
	 * @param context Context.
	 * @return List of Categories.
	 */
	public static CursorList<Category> findAll(final Context context) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return new CursorList<Category>(db.rawQuery("select *  from " + Database.Category.TABLE_NAME, null),
				Category.class);
	}

	public static Category findById(long id) {
		Category result = null;
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Database.Category.TABLE_NAME + " where " + Database.Category.ID
				+ "=?", new String[] { String.valueOf(id) });

		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			result = new Category();
			result.fromCursor(cursor);
		}

		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		id = cursor.getInt(cursor.getColumnIndex(Database.Category.ID));
		name = cursor.getString(cursor.getColumnIndex(Database.Category.NAME));
		custom = cursor.getInt(cursor.getColumnIndex(Database.Category.CUSTOM)) == 1 ? true : false;
	}

	@Override
	public void fromJSON(JSONObject jsonObject) {
		try {
			id = jsonObject.getLong("id");
			name = jsonObject.getString("name");
		} catch (JSONException e) {
		}
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(Database.Category.NAME, name);
		values.put(Database.Category.CUSTOM, 1);
		return values;
	}

	public void delete() {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		db.delete(Database.Category.TABLE_NAME, Database.Category.ID + "=?", new String[] { String.valueOf(id) });
	}

}