package com.shoulder.xiaonei.db;

import com.shoulder.xiaonei.myClass.MyStatic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_ChatLast_Helper extends SQLiteOpenHelper{
	
	private static final String CREATE_TABLE = "create table if not exists "+
			MyStatic.DB_tableName + " (_id integer primary key, "+MyStatic.DB_fieldObjectId+" varchar(63) unique not null," 
			+ MyStatic.DB_fieldHaveRead+" bit not null, "+MyStatic.DB_fieldLastMessage+" varchar(255) not null )";
	private static final String DROP_TABLE = "drop table if exists " + MyStatic.DB_tableName;
	private SQLiteDatabase db;

	public DB_ChatLast_Helper(Context context) {
		super(context, null, null, 1);
	}

	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(CREATE_TABLE);
	}
	
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	}
	
	public void insert(ContentValues values) {  
        SQLiteDatabase db = getWritableDatabase();  
        db.insert(MyStatic.DB_tableName, null, values);  
        db.close();  
    }  
	
	public Cursor query() {  
        SQLiteDatabase db = getWritableDatabase();  
        Cursor c = db.query(MyStatic.DB_tableName, null, null, null, null, null, null);  
        return c;  
    }
	
	public Cursor queryHaveNotRead(){
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery("select * from " + MyStatic.DB_tableName + " where " + MyStatic.DB_fieldHaveRead
				+"=?", new String[]{MyStatic.DB_HaveNotRead + ""});
		return c;
	}
	
	public int updataLastMessage(String objectId, ContentValues cv){
		int updateN = db.update(MyStatic.DB_tableName, cv, MyStatic.DB_fieldObjectId + "=?", new String[]{objectId});
		return updateN;
	}
	
	public void del(int id) {  
        if (db == null)  
            db = getWritableDatabase();  
        db.delete(MyStatic.DB_tableName, "_id=?", new String[] { String.valueOf(id) });  
    } 
	
	public void close() {  
        if (db != null)  
            db.close();  
    }  
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (newVersion){
	      case 2:
	        //lazy solution
	      case 1:
	        break;
	    }
	}

}
