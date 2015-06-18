package edu.hrbeu.myweather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * copy数据库到apk包
 * 
 * @author NGJ
 * 
 */
public class DBUtil extends SQLiteOpenHelper {

	private Context context;
	public static String dbName = "weather.db";// 数据库的名字
	private static String DATABASE_PATH;// 数据库在手机里的路径
	
	static final String CID = "c_id";
	static final String MyCity = "mycity";
	static final String CityCode = "cityCode";
	static final String DATABASE_TABLE = "myCityDB";
	static final String DATABASE_CREATE = 
			"create table myCityDB( _id integer primary key autoincrement, " + 
			"myCity text not null, cityCode text  );";

	private SQLiteDatabase myDataBase;
	SQLiteDatabase db;

	public DBUtil(Context context) {

		super(context, dbName, null, 1);

		this.context = context;
		String packageName = context.getPackageName();
		DATABASE_PATH = "/data/data/" + packageName + "/databases/";
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
			Log.i("tag", "The database is exist.");
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {
				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * 判断数据库是否存在
	 * 
	 * @return false or true
	 */
	public boolean checkDataBase() {
		 db = null;
		try {
			String databaseFilename = DATABASE_PATH + dbName;
			db = SQLiteDatabase.openDatabase(databaseFilename, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {

		}
		if (db != null) {
			db.close();
		}
		return db != null ? true : false;
	}

	/**
	 * 复制数据库到手机指定文件夹下
	 * 
	 * @throws IOException
	 */
	public void copyDataBase() throws IOException {
		String databaseFilenames = DATABASE_PATH + dbName;
		File dir = new File(DATABASE_PATH);
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdir();
		FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
		InputStream is = context.getResources().openRawResource(R.raw.weather);// 得到数据库文件的数据流
		byte[] buffer = new byte[8192];
		int count = 0;
		while ((count = is.read(buffer)) > 0) {
			os.write(buffer, 0, count);
			// os.flush();
		}
		os.flush();
		is.close();
		os.close();
	}

	/**
	 * 打开数据库
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DATABASE_PATH + dbName;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	/**
	 * 数据库关闭
	 */
	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	// 查询数据
	public String queryOneData(String name) {

		Cursor cursor = myDataBase.rawQuery(
				"select * from areaid_v where NAMECN ='" + name + "'", null);

		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}

		String results = cursor.getString(cursor.getColumnIndex("AREAID"));

		return results;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	//insert a contact into the database
	public  long insertmyCityDB(String mycity, String citycode)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(MyCity ,mycity);
		initialValues.put(CityCode ,citycode);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	//delete a particular contact
	public boolean deletemymyCityDB(long rowId)
	{
		return db.delete(DATABASE_TABLE, MyCity + "=" +rowId, null) > 0;
	}
	//Retrieves all the contacts
	public Cursor getAllmyCityDB()
	{
		return db.query(DATABASE_TABLE, new String[]{CID,MyCity,CityCode}, null, null, null, null, null);
	}
	//retreves a particular contact
	public Cursor getmyCityDB(String city) throws SQLException
	{
		Cursor cCursor = 
				db.query(true, DATABASE_TABLE, new String[]{ CID,
						 MyCity, CityCode}, MyCity + "='" + city+"'", null, null, null, null, null);
		if (cCursor != null)
			cCursor.moveToFirst();
		
		return cCursor;
	}
	
	
	//updates a contact
	public boolean updatemyCityDB( String mycity, String citycode)
	{
		ContentValues args = new ContentValues();
		args.put(MyCity, mycity);
		args.put(CityCode, citycode);
		return db.update(DATABASE_TABLE, args, MyCity + "='" + mycity+"'", null) > 0;
	}
}