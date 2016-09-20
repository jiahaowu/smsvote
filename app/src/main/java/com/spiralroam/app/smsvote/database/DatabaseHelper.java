package com.spiralroam.app.smsvote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	final public static String TB_NAME_CONFIG = "sms_config";
	final public static String TB_NAME_CANDIDATES = "vote_candidates";
	final public static String TB_NAME_PARTICIPANTS = "vote_participants";
	final public static String TB_NAME_PARTICIPANTS_HASH = "vote_participants_hash";
	final public static String ID = "_id";
	final public static String CONFIG_NAME = "config_name";
	final public static String CONFIG_VALUE = "config_value";
	final public static String TABLE_NAME = null;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String sqlcmd = "CREATE  TABLE IF NOT EXISTS "
				+ TB_NAME_CANDIDATES
				+ "(_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name TEXT,cnt INTEGER)";
		db.execSQL(sqlcmd);

		sqlcmd = "CREATE  TABLE IF NOT EXISTS "
				+ TB_NAME_PARTICIPANTS
				+ "(_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,phone_number INTEGER, name TEXT,cnt INTEGER)";
		db.execSQL(sqlcmd);

		sqlcmd = "CREATE  TABLE IF NOT EXISTS "
				+ TB_NAME_PARTICIPANTS_HASH
				+ "(_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,phone_number INTEGER, flag BOOLEAN)";
		db.execSQL(sqlcmd);

		sqlcmd = "CREATE  TABLE IF NOT EXISTS "
				+ TB_NAME_CONFIG
				+ "(_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,config_name TEXT, config_value INTEGER)";
		db.execSQL(sqlcmd);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);

	}

	public Cursor query(String TBL_NAME) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}

	public void addConfig(String config_name, int value) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.CONFIG_NAME, config_name);
		values.put(DatabaseHelper.CONFIG_VALUE, value);
		this.getWritableDatabase().replace(TB_NAME_CONFIG, DatabaseHelper.ID,
				values);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(CONFIG_NAME, null, null, null, null, null,
				" _id desc");
		return cursor;
	}

	public void update(int id, String Title) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ID + "=?";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(CONFIG_NAME, Title);
		db.update(TB_NAME_CONFIG, cv, where, whereValue);
	}

	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ID + "=?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(CONFIG_NAME, where, whereValue);
	}

}
