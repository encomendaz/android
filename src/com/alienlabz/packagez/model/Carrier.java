package com.alienlabz.packagez.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alienlabz.packagez.database.Database;
import com.alienlabz.packagez.util.CursorList;

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

	public static Carrier findById(Long id) {
		Carrier result = null;
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Database.Carrier.TABLE_NAME + " where " + Database.Carrier.ID
				+ "=?", new String[] { String.valueOf(id) });

		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			result = new Carrier();
			result.fromCursor(cursor);
		}

		return result;
	}

	public static List<Carrier> findAll() {
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
		try {
			name = jsonObject.getString("name");
			id = jsonObject.getLong("id");
		} catch (JSONException e) {
		}
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(Database.Carrier.ID, id);
		values.put(Database.Carrier.NAME, name);
		values.put(Database.Carrier.IMAGE_RESOURCE, logoResource);

		return values;
	}

	public void update() {
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		db.update(Database.Carrier.TABLE_NAME, toContentValues(), Database.Carrier.ID + "=?",
				new String[] { String.valueOf(id) });
	}

	@Override
	public boolean equals(Object o) {
		Carrier obj = (Carrier) o;
		return obj.id == id;
	}

	public void delete() {
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();
		db.delete(Database.Carrier.TABLE_NAME, Database.Carrier.ID + "=?", new String[] { String.valueOf(id) });
	}

}
