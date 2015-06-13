package edu.hrbeu.myweather;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hrbeu.myweather.SlideMenu;
import edu.hrbeu.myweather.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnGestureListener,
		OnTouchListener, OnClickListener {

	// 定义一个GestureDetector(手势识别类)对象的引用
	private GestureDetector myGestureDetector;
	/* private PageControlView pageControl; */
	private SlideMenu slideMenu;

	EncodeUtil jd;
	Weather myWeather = new Weather();

	TextView city;
	TextView date;
	TextView viewday;
	TextView temperature;
	TextView windD;
	TextView windP;
	ImageView phenomena;

	TextView sundown;
	TextView sunrise;

	String url;

	Button citybutton;

	int page = 0;// onfling 用于记录滑动页数，用于取消循环滑动

	// 定义一个ViewFlipper对象的引用
	ViewFlipper myViewFlipper;

	private TextView weather_condition;
	String[] WeatherCondition = { "晴", "多云", " 阴", " 阵雨", " 雷阵雨", " 雷阵雨伴有冰雹",
			" 雨夹雪", " 小雨", " 中雨", " 大雨", " 暴雨", " 大暴雨", " 特大暴雨", " 阵雪", " 小雪",
			" 中雪", " 大雪", " 暴雪", " 雾", " 冻雨", " 沙尘暴", " 小到中雨", " 中到大雨",
			" 大到暴雨", " 暴雨到大暴雨", " 大暴雨到特大暴雨", " 小到中雪", " 中到大雪", " 大到暴雪", " 浮尘",
			" 扬沙", " 强沙尘暴", " 霾", " 无" };
	String[] windDirect = { "无持续风向", "东北风", "东风", "东南风", "南风", "西南风", "西风",
			"西北风", "北风", "旋转风" };
	String[] windPower = { "微风", "3-4级", "4-5级", "5-6级", "6-7级", "7-8级",
			"8-9级", "9-10级", "10-11级", "11-12级" };

	int[] DWeatherArray = { R.drawable.d00, R.drawable.d01, R.drawable.d02,
			R.drawable.d03, R.drawable.d04, R.drawable.d05, R.drawable.d06,
			R.drawable.d07, R.drawable.d08, R.drawable.d09, R.drawable.d10,
			R.drawable.d11, R.drawable.d12, R.drawable.d13, R.drawable.d14,
			R.drawable.d15, R.drawable.d16, R.drawable.d17, R.drawable.d18,
			R.drawable.d19, R.drawable.d20, R.drawable.d21, R.drawable.d22,
			R.drawable.d23, R.drawable.d24, R.drawable.d25, R.drawable.d26,
			R.drawable.d27, R.drawable.d28, R.drawable.d29, R.drawable.d30,
			R.drawable.d31, R.drawable.d53 };

	int[] NWeatherArray = { R.drawable.n00, R.drawable.n01, R.drawable.n02,
			R.drawable.n03, R.drawable.n04, R.drawable.n05, R.drawable.n06,
			R.drawable.n07, R.drawable.n08, R.drawable.n09, R.drawable.n10,
			R.drawable.n11, R.drawable.n12, R.drawable.n13, R.drawable.n14,
			R.drawable.n15, R.drawable.n16, R.drawable.n17, R.drawable.n18,
			R.drawable.n19, R.drawable.n20, R.drawable.n21, R.drawable.n22,
			R.drawable.n23, R.drawable.n24, R.drawable.n25, R.drawable.n26,
			R.drawable.n27, R.drawable.n28, R.drawable.n29, R.drawable.n30,
			R.drawable.n31, R.drawable.n53 };

	private View view_today;

	private View view_tomorrow;

	private View view_afterday;
	private ListView citylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		
		// ////////////////////////////////////////////////////////////////
		/**
		 * 获取URL
		 */
		SharedPreferences sp;
		sp = getSharedPreferences("mycity", MODE_PRIVATE);
		String areaid = sp.getString("citycode", "101010100");
		String addsearchcity = sp.getString("searchcity", "");
		url = EncodeUtil.getUrl(areaid);
		
		// //////////////////////////////////////////////////////////////
		// 重点，将Context对象传入LayoutInflater.from()里，得到LayoutInflater对象
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);

		// 用inflate(渲染)方法将布局文件变为View对象
		view_today = factory.inflate(R.layout.view_today, null);
		view_tomorrow = factory.inflate(R.layout.view_tomorrow, null);
		view_afterday = factory.inflate(R.layout.view_afterday, null);

		// 绑定inflate控件，否则无法使用它
		myViewFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);

		/* pageControl = (PageControlView) findViewById(R.id.); */
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menuImage = (ImageView) findViewById(R.id.title_bar_menu_btn);
		menuImage.setOnClickListener(this);
		
		
		citylist = (ListView)findViewById(R.id.citylist);
		
		
		List<String> cityArray = new ArrayList<String>();
		cityArray.add(addsearchcity);
	
		ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,cityArray);

		citylist.setAdapter(cityAdapter);
		
		citylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 用addView方法将生成的View对象加入到ViewFlipper对象中
		myViewFlipper.addView(view_today);
		myViewFlipper.addView(view_tomorrow);
		myViewFlipper.addView(view_afterday);

		// MainActivity继承了OnGestureListener接口
		myGestureDetector = new GestureDetector(this);
		// 设置识别长按手势，这样才能实现拖动
		myViewFlipper.setLongClickable(true);
		// MainActivity继承了OnTouchListener接口 对myViewFlipper设置触屏事件监听器
		myViewFlipper.setOnTouchListener(this);
		myViewFlipper.setDisplayedChild(0);
		// ///////////////////////////////////////////////////////////////////

		citybutton = (Button) findViewById(R.id.citybutton);
		citybutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CityActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getWeatherDate();
		System.out.println("onResume");
	}

	public void getWeatherDate() {
		Thread newThread; // 声明一个子线程

		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				try {
					// 创建一个默认的HttpClient
					HttpClient httpclient = new DefaultHttpClient();
					// 创建一个GET请求
					HttpGet request = new HttpGet(url);
					Log.v("response text", url);
					// 发送GET请求，并将响应内容转换成字符串
					String response = httpclient.execute(request,
							new BasicResponseHandler());
					Log.v("response text", response);

					myWeather = getWeather(response);

					Message m = new Message();
					myHandler.sendMessage(m);// 发送消息:系统会自动调用handleMessage方法来处理消息

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		newThread.start(); // 启动线程
	}

	public void changeview(View view) {
		sunrise = (TextView) view.findViewById(R.id.sunrise);
		sundown = (TextView) view.findViewById(R.id.sundown);
		city = (TextView) view.findViewById(R.id.city);
		date = (TextView) view.findViewById(R.id.date);
		viewday = (TextView) view.findViewById(R.id.viewday);
		temperature = (TextView) view.findViewById(R.id.temperature);
		windD = (TextView) view.findViewById(R.id.windD);
		windP = (TextView) view.findViewById(R.id.windP);
		phenomena = (ImageView) view.findViewById(R.id.phenomena);
		// TextView city = (TextView)findViewById(R.id.city);
		weather_condition = (TextView) view
				.findViewById(R.id.weather_condition);
	}

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// if (msg.what == UpdateTextView) {
			// showText.setText("sub thread update UI");// 更新界面显示
			// }
			changeview(view_today);
			RefreshWeather(0);
			changeview(view_tomorrow);
			RefreshWeather(1);
			changeview(view_afterday);
			RefreshWeather(2);
			super.handleMessage(msg);

		}
	};

	public void RefreshWeather(int i) {
		// TODO Auto-generated method stub
		city.setText(myWeather.city);
		date.setText(myWeather.date);

		String viewdays = getDateStr(i);
		viewday.setText(viewdays);

		// 分隔出日出日落时间sunrises，sundowns(字符串格式)
		String[] suntimes = myWeather.suntime[0].split("\\|", 2);
		String sunrises, sundowns;
		sunrises = suntimes[0];
		sundowns = suntimes[1];

		sunrise.setText("日出时间:" + sunrises);
		sundown.setText("日落时间:" + sundowns);

		// ////////////////////////////////////////////
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
								// HH:mm:ss格式显示201506051830
		int hh = dt.getHours();
		Boolean dayflag = true;

		// 白天晚上的参数不同，通过日出日落时间进行判读输出
		if (hh >= 18 || hh < 8)
			dayflag = false;

		if (dayflag) {
			windD.setText(windDirect[Integer.parseInt(myWeather.windDD[i])]);
			windP.setText(windPower[Integer.parseInt(myWeather.windPD[i])]);

			phenomena.setBackgroundDrawable(getResources().getDrawable(
					DWeatherArray[Integer.parseInt(myWeather.weatherD[i])]));

			weather_condition.setText(WeatherCondition[Integer
					.parseInt(myWeather.weatherD[i])]);
			temperature.setText(myWeather.temperatureD[i]);
		} else {
			Log.v("TP", "TP4");
			windD.setText(windDirect[Integer.parseInt(myWeather.windDN[i])]);
			windP.setText(windPower[Integer.parseInt(myWeather.windPN[i])]);

			phenomena.setBackgroundDrawable(getResources().getDrawable(
					DWeatherArray[Integer.parseInt(myWeather.weatherN[i])]));

			weather_condition.setText(WeatherCondition[Integer
					.parseInt(myWeather.weatherN[i])]);
			temperature.setText(myWeather.temperatureN[i]);
		}
	}

	public Weather getWeather(String strResult) {

		try {
			String suntime;
			// /解析
			JSONObject jsonObject;
			// String a = new String(strResult, "UTF-8");
			jsonObject = new JSONObject(strResult);

			JSONObject c = jsonObject.getJSONObject("c");
			JSONObject f = jsonObject.getJSONObject("f");

			Weather weather = new Weather();
			weather.city = c.getString("c3");

			byte[] converttoBytes = weather.city.getBytes("ISO-8859-1");
			String s1 = new String(converttoBytes);
			System.out.println(s1);
			weather.city = s1;
			weather.province = c.getString("c7");
			weather.date = f.getString("f0");

			JSONArray f1 = f.getJSONArray("f1");

			for (int i = 0; i < f1.length(); i++) {

				JSONObject jsob = (JSONObject) f1.get(i);

				weather.weatherD[i] = jsob.getString("fa");
				weather.weatherN[i] = jsob.getString("fb");
				weather.temperatureD[i] = jsob.getString("fc");
				weather.temperatureN[i] = jsob.getString("fd");
				weather.windDD[i] = jsob.getString("fe");
				weather.windDN[i] = jsob.getString("ff");
				weather.windPD[i] = jsob.getString("fg");
				weather.windPN[i] = jsob.getString("fh");
				weather.suntime[i] = jsob.getString("fi");// 日出日落时间，一会用字符串处理函数分割

			}
			return weather;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * * 获取指定日后 后 dayAddNum 天的 日期
	 * 
	 * @param day 日期，格式为String："2013-9-3";
	 * 
	 * @param dayAddNum 增加天数 格式为int;
	 * 
	 * @return
	 */
	public String getDateStr(int dayAddNum) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60
				* 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}

	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	// 实现OnFling方法，就可以利用滑动的起始坐标识别出左右滑动的手势，并处理
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// 参数e1是按下事件，e2是放开事件，剩下两个是滑动的速度分量，这里用不到
		// 按下时的横坐标大于放开时的横坐标，从右向左滑动
		if (slideMenu.isMenuShow()) {
			slideMenu.hideMenu();
		} else {
			if ((e1.getX() > e2.getX()) && (page < 2)) {
				myViewFlipper.showNext();
				page += 1;
			}
			// 按下时的横坐标小于放开时的横坐标，从左向右滑动
			else if ((e1.getX() < e2.getX()) && (page > 0)) {
				myViewFlipper.showPrevious();
				page -= 1;
			} else if ((e1.getX() < e2.getX()) && (page == 0)) {
				slideMenu.showMenu();
			}

		}

		return false;
	}

	/*
	 * 实现OnTouchListener接口中的onTouch()方法，当View上发生触屏时间时调用，传如一个View和一个运动事件event，我们将
	 * 这个event传给OnGestureListener接口的onTouchEvent()方法处理，这样我们的OnFling()就能工作了
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return myGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_menu_btn:
			if (slideMenu.isMenuShow()) {
				slideMenu.hideMenu();
			} else {
				slideMenu.showMenu();
			}
			break;
		}

	}
}