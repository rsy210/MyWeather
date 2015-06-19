package edu.hrbeu.myweather;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CityActivity extends Activity {

	EditText searchcity;
	Button searchbutton;
	ListView listview;
	/*SharedPreferences sp;*/
	SharedPreferences sp2;
	DBUtil myDbHelper;
	WDataCache wDataCache;

	String mycityname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);

		/*sp = getSharedPreferences("mycity", MODE_PRIVATE);*/
		sp2 = getSharedPreferences("nowcity", MODE_PRIVATE);

		searchcity = (EditText) findViewById(R.id.searchcity);

		searchbutton = (Button) findViewById(R.id.searchbutton);
		// DBUtil myDbHelper = new DBUtil(null);
		myDbHelper = new DBUtil(this);
		wDataCache = new WDataCache(this);
		wDataCache.open();

		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}

		searchbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 查询函数，正常返回string，没有则返回null

				mycityname = searchcity.getText().toString();
				String citycode = myDbHelper.queryOneData(mycityname);
				if (citycode != null) {
					/*Editor ed = sp.edit();
					ed.putString(searchcity.getText().toString(), citycode);
					// ed.putString("searchcity", searchcity.getText()
					// .toString());
					ed.commit();*/

					Editor ed2 = sp2.edit();
					ed2.putString("citycode", citycode);
					ed2.putString("searchcity", searchcity.getText().toString());
					ed2.commit();

					Cursor cCursor = wDataCache.getmyWeatherDB(mycityname);
					if (cCursor == null || cCursor.getCount() <= 0) {
						wDataCache.insertmyWeatherDB(mycityname, citycode,
								null, null);
					} else {
						Toast.makeText(CityActivity.this,
								"你已经添加" + mycityname + "了，快去看看吧",
								Toast.LENGTH_SHORT).show();
					}

					Intent intent = new Intent(CityActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else {
					Toast.makeText(CityActivity.this, "没有找到对应城市",
							Toast.LENGTH_SHORT).show();
					searchcity.setText("");
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
