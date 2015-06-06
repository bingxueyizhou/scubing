package com.scufy.scubing.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	/*���ݿ��
	ID|�γ̺�|�����|�γ���|Ӣ����|ѧ��|��ѡ��|�ɼ�|ѧ��
	//*/
	public static final String KEY_ID 			= "_id";
	public static final String KEY_CLASS_NUM 	= "class_num";
	public static final String KEY_CLASS_ID 	= "class_id";
	public static final String KEY_CLASS_NAME 	= "class_name";
	public static final String KEY_CLASS_ENG 	= "class_eng";
	public static final String KEY_POINT 		= "point";
	public static final String KEY_TYPE 		= "type";
	public static final String KEY_GRADE 		= "grade";
	public static final String KEY_TERM 		= "term";
	
	private static final String TAG 			= "DBADAPTER";
	
	private static final String DATABASE_NAME = "cache.db";
	private static final String DATABASE_TABLE = "allgrade";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE =  "create table allgrade (" +
													"_id integer primary key autoincrement," + 
													"class_num varchar(16) not null," + 
													"class_id integer not null,"+
													"class_name varchar(64) not null," +
													"class_eng varchar(64), " + 
													"point float not null," + 
													"type varchar(16) not null,"+
													"grade float not null," + 
													"term varchar(64) not null);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	public DBAdapter(Context ctx) {
		//Log.i("ϵͳ","�Ѿ��������ݿ���");
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		//Log.i("ϵͳ","���ݿ�"+DATABASE_CREATE);
	}  
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		}  
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("ϵͳ","���ݿ⽨��");
			db.execSQL(DATABASE_CREATE);
		} 
		
		@Override  
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " 
						+ newVersion + ", which will destroy all old data"); 
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		} 
	}
	
	//---�����ݿ�---   
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}  
	
	//---�ر����ݿ�---   
	public void close() {
		DBHelper.close();
	}
	
	//---�����ݿ��в���һ������---
	//ID|�γ̺�|�����|�γ���|Ӣ����|ѧ��|��ѡ��|�ɼ�|ѧ��|��������
	public long insertTitle(String class_num,		int class_id, 	String class_name,
							String class_Eng, 	float point, 	String type,
							float grade, 		String term) {
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CLASS_NUM, class_num);
		initialValues.put(KEY_CLASS_ID, class_id);
		initialValues.put(KEY_CLASS_NAME, class_name);
		initialValues.put(KEY_CLASS_ENG, class_Eng);
		initialValues.put(KEY_POINT, point);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_GRADE, grade);
		initialValues.put(KEY_TERM, term);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}  
	
	//---ɾ��һ��ָ������---   
	public boolean deleteTitle(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0; 
	}  
	
	//---�������б���---   
	public Cursor getAllTitles() {
		return db.query(DATABASE_TABLE, 
				new String[] { KEY_ID,KEY_CLASS_NUM,KEY_CLASS_ID,
								KEY_CLASS_NAME,KEY_CLASS_ENG,KEY_POINT,
								KEY_TYPE,KEY_GRADE,KEY_TERM}, 
				null, null, null, null, null); 
	}
	
	//---����һ��ָ������---  
	public Cursor getTitle(long rowId) throws SQLException {
		Cursor mCursor =  db.query(true, DATABASE_TABLE, 
				new String[] { KEY_ID, KEY_ID,KEY_CLASS_NUM,KEY_CLASS_ID,
								KEY_CLASS_NAME,KEY_CLASS_ENG,KEY_POINT,
								KEY_TYPE,KEY_GRADE,KEY_TERM}, 
				KEY_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}  
		
		return mCursor; 
	}  
	
	//---����һ������---   
	public boolean updateTitle(int rowId,
								String class_num,		int class_id, 	String class_name,
								String class_Eng, 	float point, 	String type,
								float grade, 		String term) {
		ContentValues args = new ContentValues(); 
		args.put(KEY_CLASS_NUM, class_num);
		args.put(KEY_CLASS_ID, class_id);
		args.put(KEY_CLASS_NAME, class_name);
		args.put(KEY_CLASS_ENG, class_Eng);
		args.put(KEY_POINT, point);
		args.put(KEY_TYPE, type);
		args.put(KEY_GRADE, grade);
		args.put(KEY_TERM, term);
		return db.update(DATABASE_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
	}
	
	//---ɾ����������---
	public int clearALL(){
		return db.delete(DATABASE_TABLE, null, null);
	}
}
