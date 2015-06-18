package edu.hrbeu.myweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WDataCache {

	static final String ID = "_id";
	static final String City = "city";
	static final String Weather = "weather";
	static final String WIndex= "windex";
	
	static final String TAG = "WDataCache";
	
	static final String DATABASE_NAME = "MyDB.db";
	static final String DATABASE_TABLE = "myWeatherDB";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE = 
			"create table myWeatherDB( _id integer primary key autoincrement, " + 
			"city text not null, weather text  ,windex text  );";
	private Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public WDataCache(Context cxt)
	{
		this.context = cxt;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try
			{
				db.execSQL(DATABASE_CREATE);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.wtf(TAG, "Upgrading database from version "+ oldVersion + "to "+
			 newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS myWeatherDB");
			onCreate(db);
		}
	}
	
	//open the database
	public WDataCache open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	//close the database
	public void close()
	{
		DBHelper.close();
	}
	
	//insert a contact into the database
	public  long insertmyWeatherDB(String city, String weather,String windex)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(City ,city);
		initialValues.put(Weather ,weather);
		initialValues.put(WIndex ,windex);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	//delete a particular contact
	public boolean deletemyWeatherDB(long rowId)
	{
		return db.delete(DATABASE_TABLE, City + "=" +rowId, null) > 0;
	}
	//Retrieves all the contacts
	public Cursor getAllmyWeatherDB()
	{
		return db.query(DATABASE_TABLE, new String[]{ID,City,Weather,WIndex}, null, null, null, null, null);
	}
	//retreves a particular contact
	public Cursor getmyWeatherDB(String city) throws SQLException
	{
		Cursor wCursor = 
				db.query(true, DATABASE_TABLE, new String[]{ ID,
						 City, Weather, WIndex}, City + "='" + city+"'", null, null, null, null, null);
		if (wCursor != null)
			wCursor.moveToFirst();
		
		return wCursor;
	}
	
	
	//updates a contact
	public boolean updatemyWeatherDB( String city, String weather, String windex)
	{
		ContentValues args = new ContentValues();
		args.put(City, city);
		args.put(Weather, weather);
		args.put(WIndex, windex);
		return db.update(DATABASE_TABLE, args, City + "='" + city+"'", null) > 0;
	}
/*	public boolean updatemyWeatherDB2( String city, String windex)
	{
		ContentValues args = new ContentValues();
		args.put(City, city);

		args.put(WIndex, windex);
		return db.update(DATABASE_TABLE, args, City + "='" + city+"'", null) > 0;
	}*/
}