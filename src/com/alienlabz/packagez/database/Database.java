package com.alienlabz.packagez.database;

import com.alienlabz.packagez.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Database Utility Class.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class Database extends SQLiteOpenHelper {

	public static Context context;

	/**
	 * Database Name.
	 */
	public static final String DATABASE_NAME = "database.db";

	/**
	 * Latest database version.
	 */
	public static final int VERSION = 1;

	/**
	 * Constants for Tracking Table.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public static final class Tracking {
		public static final String TABLE_NAME = "tracking";
		public static final String ID = "_id";
		public static final String DATE = "date";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String STATUS = "status";
		public static final String DESCRIPTION = "desc";
		public static final String CODE = "code";
	}

	/**
	 * Constants for Category Table.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public static final class Category {
		public static final String TABLE_NAME = "category";
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String CUSTOM = "custom";
	}

	/**
	 * Constants for Carrier Table.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public static final class Carrier {
		public static final String TABLE_NAME = "carrier";
		public static final String ID = "_id";
		public static final String IMAGE_RESOURCE = "image_resource";
		public static final String NAME = "name";
	}

	/**
	 * Constants for Packages Table.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public static final class Package {
		public static final String TABLE_NAME = "packages";
		public static final String ID = "_id";
		public static final String CARRIER_ID = "carrier_id";
		public static final String DESCRIPTION = "description";
		public static final String CODE = "code";
		public static final String DELIVERED = "delivered";
		public static final String TAXED = "taxed";
		public static final String LAST_DATE = "lastdate";
		public static final String LAST_STATUS = "laststatus";
		public static final String FIRST_DATE = "firstdate";
		public static final String CATEGORY_ID = "category_id";
		public static final String DELETED = "deleted";
	}

	/**
	 * Singleton Instance.
	 */
	private static Database instance = null;

	/**
	 * Get Singleton Instance.
	 * 
	 * @param context Context.
	 * @return Single instance of this class.
	 */
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database(context, VERSION);
		}
		return instance;
	}

	public static void initialize(Context pContext) {
		context = pContext;
	}

	/**
	 * Private Constructor: following Singleton Pattern.
	 * 
	 * @param context Context.
	 * @param version Version.
	 */
	private Database(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
	}

	/**
	 * Creating our tables.
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {
		createPackageTable(db);
		createTrackingTable(db);
		createCarrierTable(db);
		createCategoryTable(db);
	}

	private void createPackageTable(final SQLiteDatabase db) {
		final StringBuilder packages = new StringBuilder();
		packages.append("CREATE TABLE ");
		packages.append(Package.TABLE_NAME);
		packages.append("(");
		packages.append(Package.ID);
		packages.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		packages.append(Package.DELETED);
		packages.append(" INTEGER,");
		packages.append(Package.CARRIER_ID);
		packages.append(" INTEGER,");
		packages.append(Package.CATEGORY_ID);
		packages.append(" INTEGER,");
		packages.append(Package.DESCRIPTION);
		packages.append(" TEXT,");
		packages.append(Package.CODE);
		packages.append(" TEXT,");
		packages.append(Package.LAST_DATE);
		packages.append(" TEXT,");
		packages.append(Package.FIRST_DATE);
		packages.append(" TEXT,");
		packages.append(Package.DELIVERED);
		packages.append(" INTEGER,");
		packages.append(Package.LAST_STATUS);
		packages.append(" TEXT,");
		packages.append(Package.TAXED);
		packages.append(" INTEGER");
		packages.append(")");
		db.execSQL(packages.toString());
	}

	private void createTrackingTable(final SQLiteDatabase db) {
		final StringBuilder itens = new StringBuilder();
		itens.append("CREATE TABLE ");
		itens.append(Tracking.TABLE_NAME);
		itens.append("(");
		itens.append(Tracking.ID);
		itens.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		itens.append(Tracking.DATE);
		itens.append(" TEXT,");
		itens.append(Tracking.CITY);
		itens.append(" TEXT,");
		itens.append(Tracking.STATE);
		itens.append(" TEXT,");
		itens.append(Tracking.STATUS);
		itens.append(" TEXT,");
		itens.append(Tracking.CODE);
		itens.append(" TEXT,");
		itens.append(Tracking.DESCRIPTION);
		itens.append(" TEXT)");
		db.execSQL(itens.toString());
	}

	private void createCategoryTable(final SQLiteDatabase db) {
		final StringBuilder category = new StringBuilder();
		category.append("CREATE TABLE ");
		category.append(Category.TABLE_NAME);
		category.append("(");
		category.append(Category.ID);
		category.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		category.append(Category.CUSTOM);
		category.append(" INTEGER,");
		category.append(Category.NAME);
		category.append(" TEXT)");
		db.execSQL(category.toString());
		loadCategories(db);
	}

	private void createCarrierTable(final SQLiteDatabase db) {
		final StringBuilder carrier = new StringBuilder();
		carrier.append("CREATE TABLE ");
		carrier.append(Carrier.TABLE_NAME);
		carrier.append("(");
		carrier.append(Carrier.ID);
		carrier.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		carrier.append(Carrier.NAME);
		carrier.append(" TEXT,");
		carrier.append(Carrier.IMAGE_RESOURCE);
		carrier.append(" LONG)");
		db.execSQL(carrier.toString());
		loadCarriers(db);
	}

	private void loadCategories(final SQLiteDatabase db) {
		SQLiteStatement stmt = db.compileStatement("insert into " + Category.TABLE_NAME + " (" + Category.NAME + ","
				+ Category.CUSTOM + ") values (?,?)");

		stmt.bindString(1, context.getResources().getString(R.string.default_category));
		stmt.bindLong(2, 0);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, context.getResources().getString(R.string.category_my_packages));
		stmt.bindLong(2, 0);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, context.getResources().getString(R.string.category_sent_packages));
		stmt.bindLong(2, 0);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, context.getResources().getString(R.string.category_christmas_packages));
		stmt.bindLong(2, 0);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, context.getResources().getString(R.string.category_archived));
		stmt.bindLong(2, 0);
		stmt.execute();
	}

	private void loadCarriers(final SQLiteDatabase db) {
		SQLiteStatement stmt = db.compileStatement("insert into " + Carrier.TABLE_NAME + " (" + Carrier.NAME + ","
				+ Carrier.IMAGE_RESOURCE + ") values (? ,?)");

		stmt.bindString(1, "Correios");
		stmt.bindLong(2, R.drawable.carrier_br);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, "USPS");
		stmt.bindLong(2, R.drawable.carrier_us);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, "Royal Mail");
		stmt.bindLong(2, R.drawable.carrier_gb);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, "China Post");
		stmt.bindLong(2, R.drawable.carrier_cn);
		stmt.execute();

		stmt.clearBindings();
		stmt.bindString(1, "Hong Kong Post");
		stmt.bindLong(2, R.drawable.carrier_hk);
		stmt.execute();
	}

	/**
	 * Nothing to do.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
