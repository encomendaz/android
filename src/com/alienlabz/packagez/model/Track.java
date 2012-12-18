package com.alienlabz.packagez.model;

import java.util.Locale;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alienlabz.packagez.database.Database;
import com.alienlabz.packagez.util.CursorList;

/**
 * Model for Trackings.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
final public class Track extends Model<Track> {
	public String code;
	public String city;
	public String state;
	public Status status;
	public String date;
	public String description;

	/**
	 * Get trackings from a Package.
	 * 
	 * @param context Context.
	 * @param code Package Code. 
	 * @return List of Trackings.
	 */
	public static CursorList<Track> findByCode(final String code) {
		final SQLiteDatabase db = Database.getInstance().getReadableDatabase();
		return new CursorList<Track>(db.rawQuery("select * from " + Database.Tracking.TABLE_NAME + " where "
				+ Database.Tracking.CODE + " =? order by " + Database.Tracking.ID + " DESC",
				new String[] { code.toUpperCase(Locale.ENGLISH) }), Track.class);
	}

	/**
	 * Delete all trackings of a package.
	 * 
	 * @param context Context.
	 * @param code Package Code.
	 */
	public static void deleteByCode(final String code) {
		Database.getInstance()
				.getWritableDatabase()
				.delete(Database.Tracking.TABLE_NAME, Database.Tracking.CODE + "=?",
						new String[] { code.toUpperCase(Locale.ENGLISH) });
	}

	/**
	 *  Insert this Tracking.
	 *  
	 * @param context Context.
	 */
	public void insert() {
		id = Database.getInstance().getWritableDatabase()
				.insertOrThrow(Database.Tracking.TABLE_NAME, null, toContentValues());
	}

	@Override
	public void fromCursor(Cursor cursor) {
		id = cursor.getLong(cursor.getColumnIndex(Database.Tracking.ID));
		code = cursor.getString(cursor.getColumnIndex(Database.Tracking.CODE));
		description = cursor.getString(cursor.getColumnIndex(Database.Tracking.DESCRIPTION));
		date = cursor.getString(cursor.getColumnIndex(Database.Tracking.DATE));
		state = cursor.getString(cursor.getColumnIndex(Database.Tracking.STATE));
		city = cursor.getString(cursor.getColumnIndex(Database.Tracking.CITY));
		status = Status.fromString(cursor.getString(cursor.getColumnIndex(Database.Tracking.STATUS)));
	}

	@Override
	public void fromJSON(JSONObject jsonObject) {
	}

	@Override
	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();
		values.put(Database.Tracking.CITY, city);
		values.put(Database.Tracking.DATE, date);
		values.put(Database.Tracking.DESCRIPTION, description);
		values.put(Database.Tracking.CODE, code.toUpperCase(Locale.ENGLISH));
		values.put(Database.Tracking.STATE, state);
		values.put(Database.Tracking.STATUS, status.toString());
		return values;
	}
}
