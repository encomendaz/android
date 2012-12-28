package com.alienlabz.packagez.model;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Classe de Modelo.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 *
 * @param <T>
 */
abstract public class Model<T> {
	public long id;

	/**
	 * Preencher o objeto com os dados de um cursor.
	 * 
	 * @param cursor Cursor.
	 */
	public abstract void fromCursor(Cursor cursor);

	/**
	 * Preencher o objeto com os dados de um JSONObject.
	 * 
	 * @param jsonObject JSONObject.
	 */
	public abstract void fromJSON(JSONObject jsonObject);

	/**
	 * Retornar um ContentValues a partir dos dados do objeto.
	 * 
	 * @return ContentValues.
	 */
	public abstract ContentValues toContentValues();
	
}
