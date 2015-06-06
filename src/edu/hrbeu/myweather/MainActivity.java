package edu.hrbeu.myweather;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	EncodeUtil jd;
	Weather myWeather = new Weather();

	TextView city;
	TextView date;
	TextView temperature;
	TextView windD;
	TextView windP;
	ImageView phenomena;
	
	int[] DWeatherArray = {R.drawable.n00,R.drawable.n01,R.drawable.n02,R.drawable.n03,R.drawable.n04};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		city = (TextView) findViewById(R.id.city);
		
		date = (TextView) findViewById(R.id.date);
		temperature = (TextView) findViewById(R.id.temperature);
		windD = (TextView) findViewById(R.id.windD);
		windP = (TextView) findViewById(R.id.windP);
		phenomena = (ImageView)findViewById(R.id.phenomena);
		// TextView city = (TextView)findViewById(R.id.city);

		jd = new EncodeUtil();

		System.out.println(jd.url);

		Thread newThread; // 声明一个子线程

		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				try {
					// 创建一个默认的HttpClient
					HttpClient httpclient = new DefaultHttpClient();
					// 创建一个GET请求
					HttpGet request = new HttpGet(jd.url);
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

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// if (msg.what == UpdateTextView) {
			// showText.setText("sub thread update UI");// 更新界面显示
			// }
			RefreshWeather();
			super.handleMessage(msg);

		}
	};

	public void RefreshWeather() {
		// TODO Auto-generated method stub
		city.setText(myWeather.city);
		date.setText(myWeather.date);
		temperature.setText(myWeather.temperatureD[0]);
		windD.setText(myWeather.windDD[0]);
		windP.setText(myWeather.windPD[0]);
		//phenomena(getResources().getDrawable(DWeatherArray[weather.weatherD[0]]));
	}

	public Weather getWeather(String strResult) {

		try {
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

			}

			return weather;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
}
