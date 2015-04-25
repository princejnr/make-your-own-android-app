package com.td.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.td.rssreader.parser.RSSFeed;
import com.td.rssreader.parser.RSSItem;

public class DBHandler {
	
	private static String DB_NAME = "feedDB";
	private static int DB_VERSION = 1;
	private static String TABLE_NAME = "feedsTable";

	
	private static String COL_FEED_TITLE = "FEED_TITLE";
	private static String COL_FEED_CONTENT = "FEED_CONTENT";
	private static String COL_FEED_DATE = "FEED_DATE";
		
	
	private DBHelper DBH;
	private final Context c;
	private SQLiteDatabase DB;
	
	
	public DBHandler(Context c){
		this.c = c;
	}
	
	private DBHandler open() {

		this.DBH = new DBHelper(c);
		this.DB = this.DBH.getWritableDatabase();
		return this;
	}
	
	private void close() {
		DBH.close();
	}
	
	
	public RSSFeed getFeedItems(){
		RSSFeed retFeed = new RSSFeed();
		this.open();
		
		String[] columns = new String[] { COL_FEED_CONTENT, COL_FEED_DATE, COL_FEED_TITLE};

		Cursor c = this.DB.query(TABLE_NAME, columns, null, null, null, null,
				null);
		
		RSSItem item;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			item = new RSSItem();

			item.setDate(c.getString(c.getColumnIndex(COL_FEED_DATE)));
			
			item.setDescription(c.getString(c.getColumnIndex(COL_FEED_CONTENT)));
			
			item.setTitle(c.getString(c.getColumnIndex(COL_FEED_TITLE)));

			retFeed.addItem(item);
		}
		this.close();
		return retFeed;
		
		
	}
	
	public void addFeed(RSSFeed feed){
		
		this.open();
		
		//clearing previous data 
		DB.execSQL(String.format("DELETE FROM %s",TABLE_NAME));
		Log.i("DB log", "adding new feed");
		for (int i = 0; i < feed.getItemCount(); i++){
			
			ContentValues cv = new ContentValues();
			cv.put(COL_FEED_TITLE, feed.getItem(i).getTitle());
			cv.put(COL_FEED_CONTENT, feed.getItem(i).getDescription());
			cv.put(COL_FEED_DATE, feed.getItem(i).getDate());	
			
			this.DB.insert(TABLE_NAME, null, cv);
		}
		this.close();
		Log.i("DB log", "done");
		
	}
	
	
	public class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context c) {
			super(c, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try{
				db.execSQL(String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, COL_FEED_TITLE , COL_FEED_CONTENT, COL_FEED_DATE));
			}catch(Exception e){
				Log.e("databse error-oncreate", e.getMessage());
			}
		
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
