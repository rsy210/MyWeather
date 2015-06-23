package edu.hrbeu.myweather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private SQLiteDatabase myDataBase;
	
	private String AREAID="areaid";
	private String NAMEEN="nameen";
	private String NAMECN="namecn";
	private String DISTRICTEN="districten";
	private String DISTRICTCN="districtcn";
	private String PROVEN="proven";
	private String PROVCN="provcn";
	private String NATIONEN="nationen";
	private String NATIONCN="nationcn";
	
	private String DATABASE_TABLE="areaid_v";
	
	
	

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
		SQLiteDatabase db = null;
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

	



	public ArrayList<String> selectCity(String key) {

//		if (!myDataBase.isOpen()) {
//			myDataBase = myDataBase.openDataBase();
//		}

		ArrayList<String> stockList = new ArrayList<String>();

		Cursor cur = null;

		if (null != key && !"".equals(key)) {

			// 查询的列字段名
			String[] columns = { NAMEEN, NAMECN };
		
			// 查询条件
			String where = NAMEEN + " like ? or " + NAMECN + " like ? ";
			// 查询参数
			String[] selectArgs = { key + "%", key + "%" };
			// 执行查询
			cur = myDataBase.query(DATABASE_TABLE, columns, where, selectArgs, null,
					null, null);

			cur.moveToFirst();
			// 循环读取数据
			while (!cur.isAfterLast()) {
				String nameen = cur.getString(0);
				String namecn = cur.getString(1);
				stockList.add( namecn);
				cur.moveToNext();
			}
			cur.close();
			//close();
			return stockList;
		}
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}