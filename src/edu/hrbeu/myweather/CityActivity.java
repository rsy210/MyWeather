package edu.hrbeu.myweather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class CityActivity extends Activity {

	EditText searchcity;
	ListView searchlist;
	/* SharedPreferences sp; */
	SharedPreferences sp2;
	DBUtil myDbHelper;
	WDataCache wDataCache;

	String mycityname;

	ArrayList<String> nowlists;

	TextView myLocation;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	String locaName;

	// TextView hotcity11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		// 隐藏软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* sp = getSharedPreferences("mycity", MODE_PRIVATE); */
		sp2 = getSharedPreferences("nowcity", MODE_PRIVATE);

		searchcity = (EditText) findViewById(R.id.searchcity);

		searchlist = (ListView) findViewById(R.id.searchlist);

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

		/*
		 * searchbutton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // 查询函数，正常返回string，没有则返回null
		 * 
		 * mycityname = searchcity.getText().toString(); addCity(mycityname); }
		 * });
		 */

		searchcity.addTextChangedListener((new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String searchcityV = searchcity.getText().toString();
				if (searchcityV != null && !"".equals(searchcityV.trim())) {
					// Log.i(TAG , " searchcityV:"+searchcityV);
					nowlists = myDbHelper.selectCity(searchcityV);// 根据关键字查询
					ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
							CityActivity.this, R.layout.cityoflist_style, nowlists);
					searchlist.setAdapter(arrayAdapter);
					searchlist
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									String cityname = nowlists.get(position);
									addCity(cityname);
								}
							});
				} else {
					searchlist.setAdapter(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		}));

		/*
		 * // 注册广播 IntentFilter filter = new IntentFilter();
		 * filter.addAction(Common.LOCATION_ACTION); this.registerReceiver(new
		 * LocationBroadcastReceiver(), filter);
		 * 
		 * // 启动服务 Intent intent = new Intent(); intent.setClass(this,
		 * LocationSvc.class); startService(intent);
		 */
		myLocation = (TextView) findViewById(R.id.mylocation);
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		myLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitLocation();
				mLocationClient.start();

			}
		});

		/*
		 * // 等待提示 dialog = new ProgressDialog(this);
		 * dialog.setMessage("正在定位..."); dialog.setCancelable(true);
		 * dialog.show();
		 */
		TextView hotcity11 = (TextView) findViewById(R.id.hotcity11);
		hotcityarr(hotcity11);
		TextView hotcity12 = (TextView) findViewById(R.id.hotcity12);
		hotcityarr(hotcity12);
		TextView hotcity13 = (TextView) findViewById(R.id.hotcity13);
		hotcityarr(hotcity13);
		TextView hotcity21 = (TextView) findViewById(R.id.hotcity21);
		hotcityarr(hotcity21);
		TextView hotcity22 = (TextView) findViewById(R.id.hotcity22);
		hotcityarr(hotcity22);
		TextView hotcity23 = (TextView) findViewById(R.id.hotcity23);
		hotcityarr(hotcity23);
		TextView hotcity31 = (TextView) findViewById(R.id.hotcity31);
		hotcityarr(hotcity31);
		TextView hotcity32 = (TextView) findViewById(R.id.hotcity32);
		hotcityarr(hotcity32);
		TextView hotcity33 = (TextView) findViewById(R.id.hotcity33);
		hotcityarr(hotcity33);
		TextView hotcity41 = (TextView) findViewById(R.id.hotcity41);
		hotcityarr(hotcity41);
		TextView hotcity42 = (TextView) findViewById(R.id.hotcity42);
		hotcityarr(hotcity42);
		TextView hotcity43 = (TextView) findViewById(R.id.hotcity43);
		hotcityarr(hotcity43);
		TextView hotcity51 = (TextView) findViewById(R.id.hotcity51);
		hotcityarr(hotcity51);
		TextView hotcity52 = (TextView) findViewById(R.id.hotcity52);
		hotcityarr(hotcity52);
		TextView hotcity53 = (TextView) findViewById(R.id.hotcity53);
		hotcityarr(hotcity53);
		TextView hotcity61 = (TextView) findViewById(R.id.hotcity61);
		hotcityarr(hotcity61);
		TextView hotcity62 = (TextView) findViewById(R.id.hotcity62);
		hotcityarr(hotcity62);
		TextView hotcity63 = (TextView) findViewById(R.id.hotcity63);
		hotcityarr(hotcity63);
		TextView hotcity71 = (TextView) findViewById(R.id.hotcity71);
		hotcityarr(hotcity71);
		TextView hotcity72 = (TextView) findViewById(R.id.hotcity72);
		hotcityarr(hotcity72);
		TextView hotcity73 = (TextView) findViewById(R.id.hotcity73);
		hotcityarr(hotcity73);
		TextView hotcity81 = (TextView) findViewById(R.id.hotcity81);
		hotcityarr(hotcity81);
		TextView hotcity82 = (TextView) findViewById(R.id.hotcity82);
		hotcityarr(hotcity82);
		TextView hotcity83 = (TextView) findViewById(R.id.hotcity83);
		hotcityarr(hotcity83);
		TextView hotcity91 = (TextView) findViewById(R.id.hotcity91);
		hotcityarr(hotcity91);
		TextView hotcity92 = (TextView) findViewById(R.id.hotcity92);
		hotcityarr(hotcity92);
		TextView hotcity93 = (TextView) findViewById(R.id.hotcity93);
		hotcityarr(hotcity93);
	}

	public void hotcityarr(final TextView hotcity) {

		hotcity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hotcityname = hotcity.getText().toString();
				addCity(hotcityname);
			}
		});
	}

	public void addCity(String mycityname) {
		String citycode = myDbHelper.queryOneData(mycityname);
		if (citycode != null) {
			/*
			 * Editor ed = sp.edit();
			 * ed.putString(searchcity.getText().toString(), citycode); //
			 * ed.putString("searchcity", searchcity.getText() // .toString());
			 * ed.commit();
			 */

			Editor ed2 = sp2.edit();
			ed2.putString("citycode", citycode);
			ed2.putString("searchcity", mycityname);
			ed2.commit();

			Cursor cCursor = wDataCache.getmyWeatherDB(citycode);
			if (cCursor == null || cCursor.getCount() <= 0) {
				wDataCache.insertmyWeatherDB(mycityname, citycode, null, null);
			} else {
				Toast.makeText(CityActivity.this,
						"你已经添加" + mycityname + "了，快去看看吧", Toast.LENGTH_SHORT)
						.show();
			}

			Intent intent = new Intent(CityActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else {
			Toast.makeText(CityActivity.this, "没有找到对应城市", Toast.LENGTH_SHORT)
					.show();
			searchcity.setText("");
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			String areaid = sp2.getString("citycode", null);

			if (areaid == null) {

				Toast.makeText(CityActivity.this, "请添加一个城市", Toast.LENGTH_SHORT)
						.show();
			} else {
				finish();
			}
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * private class LocationBroadcastReceiver extends BroadcastReceiver {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { if
	 * (!intent.getAction().equals(Common.LOCATION_ACTION)) return; String
	 * locationInfo = intent.getStringExtra(Common.LOCATION);
	 * location.setText(locationInfo); dialog.dismiss();
	 * CityActivity.this.unregisterReceiver(this);// 不需要时注销 } }
	 */

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				locaName = location.getAddrStr();
				myLocation.setText("定位结果：" + locaName);

				sb.append("\nprovince");
				// 省
				sb.append(location.getProvince());
				// 市
				sb.append("\ncity");
				sb.append(location.getCity());
				// 县和区
				sb.append("\ndistrict");
				sb.append(location.getDistrict());
				String locaCityName = location.getCity();
				locaCityName = locaCityName.substring(0,
						locaCityName.length() - 1);
				String locaDistrictName = location.getDistrict();
				if (locaDistrictName.length() >= 3) {
					locaDistrictName = locaDistrictName.substring(0,
							locaDistrictName.length() - 1);
				}

				String citycode = myDbHelper.queryOneData(locaDistrictName);
				if (citycode != null) {
					addCity(locaDistrictName);
				} else {
					addCity(locaCityName);
				}

			}

			// logMsg(sb.toString());
			Log.i("CCCC", sb.toString());
			mLocationClient.stop();

		}
	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	// public void logMsg(String str) {
	// try {
	// if (mLocationResult != null)
	// mLocationResult.setText(str);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		// int span=1000;
		int span = 5000;
		// try {
		// span = Integer.valueOf(frequence.getText().toString());
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		// option.setIsNeedAddress(checkGeoLocation.isChecked());
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
}
