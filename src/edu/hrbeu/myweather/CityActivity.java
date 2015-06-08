package edu.hrbeu.myweather;

import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CityActivity extends Activity {

	EditText searchcity;
	Button searchbutton;
	ListView listview;
	SharedPreferences sp;
	DBUtil myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);

		sp = getSharedPreferences("mycity", MODE_PRIVATE);

		searchcity = (EditText) findViewById(R.id.searchcity);
		searchbutton = (Button) findViewById(R.id.searchbutton);
		

		// DBUtil myDbHelper = new DBUtil(null);
	myDbHelper = new DBUtil(this);

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
				String citycode = myDbHelper.queryOneData(searchcity.getText()
						.toString());

				Editor ed = sp.edit();
				ed.putString(citycode, searchcity.getText()
						.toString());
				ed.commit();
				finish();
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
