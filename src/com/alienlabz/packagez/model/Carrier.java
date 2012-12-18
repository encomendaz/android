package com.alienlabz.packagez.model;

import java.util.List;

import org.json.JSONObject;

import com.alienlabz.packagez.database.Database;
import com.alienlabz.packagez.util.CursorList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Carriers.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class Carrier extends Model<Carrier> {
	public String name;
	public int logoResource;

	public Carrier(final String name, final int resource) {
		this.name = name;
		this.logoResource = resource;
	}

	public Carrier() {
	}

	public void insert() {
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		db.insert(Database.Carrier.TABLE_NAME, null, toContentValues());
	}

	public static Carrier findById(int int1) {
		Carrier result = null;
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Database.Carrier.TABLE_NAME + " where " + Database.Carrier.ID
				+ "=?", new String[] { String.valueOf(int1) });

		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			result = new Carrier();
			result.fromCursor(cursor);
		}

		return result;
	}

	public static List<Carrier> findAll(final Context context) {
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		return new CursorList<Carrier>(db.rawQuery("select * from " + Database.Carrier.TABLE_NAME, null), Carrier.class);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void fromCursor(Cursor cursor) {
		id = cursor.getLong(cursor.getColumnIndex(Database.Carrier.ID));
		name = cursor.getString(cursor.getColumnIndex(Database.Carrier.NAME));
		logoResource = cursor.getInt(cursor.getColumnIndex(Database.Carrier.IMAGE_RESOURCE));
	}

	@Override
	public void fromJSON(JSONObject jsonObject) {
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(Database.Carrier.NAME, name);
		values.put(Database.Carrier.IMAGE_RESOURCE, logoResource);

		return values;
	}

}
