package edu.hrbeu.myweather;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
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
	String url;
	Date dt;
	
	private TextView weather_condition;
	String[] WeatherCondition = { "��","����", " ��", " ����", " ������", " ��������б���",
			" ���ѩ", " С��", " ����", " ����", " ����", " ����", " �ش���", " ��ѩ", " Сѩ",
			" ��ѩ", " ��ѩ", " ��ѩ", " ��", " ����", " ɳ����", " С������", " �е�����",
			" �󵽱���", " ���굽����", " ���굽�ش���", " С����ѩ", " �е���ѩ", " �󵽱�ѩ", " ����",
			" ��ɳ", " ǿɳ����", " ��", " ��" };
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String areaid = "101010100";
		String type = "forecast_v";
		String appid = "c2ffc8e63c5b40ca";
		String appid_six = "c2ffc8";
		String private_key = "0244f8_SmartWeatherAPI_5e9551e";

		dt = new Date();// �������Ҫ��ʽ,��ֱ����dt,dt���ǵ�ǰϵͳʱ��
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// ������ʾ��ʽ
		String nowTime = "";
		nowTime = df.format(dt);// ��DateFormat��format()������dt�л�ȡ����yyyy/MM/dd
								// HH:mm:ss��ʽ��ʾ
		System.out.println("nowTime:" + nowTime);

		// ��Ҫ���ܵ�����
		String public_key = "http://open.weather.com.cn/data/?areaid=" + areaid
				+ "&type=" + type + "&date=" + nowTime + "&appid=" + appid;

		String key = EncodeUtil.standardURLEncoder(public_key, private_key);

		// System.out.println(str);
		url = "http://open.weather.com.cn/data/?areaid=" + areaid + "&type="
				+ type + "&date=" + nowTime + "&appid=" + appid_six + "&key="
				+ key;

		city = (TextView) findViewById(R.id.city);

		date = (TextView) findViewById(R.id.date);
		temperature = (TextView) findViewById(R.id.temperature);
		windD = (TextView) findViewById(R.id.windD);
		windP = (TextView) findViewById(R.id.windP);
		phenomena = (ImageView) findViewById(R.id.phenomena);
		// TextView city = (TextView)findViewById(R.id.city);
		weather_condition = (TextView) findViewById(R.id.weather_condition);

		Thread newThread; // ����һ�����߳�

		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// ����д�����߳���Ҫ���Ĺ���
				try {
					// ����һ��Ĭ�ϵ�HttpClient
					HttpClient httpclient = new DefaultHttpClient();
					// ����һ��GET����
					HttpGet request = new HttpGet(url);
					// ����GET���󣬲�����Ӧ����ת�����ַ���
					String response = httpclient.execute(request,
							new BasicResponseHandler());
					Log.v("response text", response);

					myWeather = getWeather(response);

					Message m = new Message();
					myHandler.sendMessage(m);// ������Ϣ:ϵͳ���Զ�����handleMessage������������Ϣ

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		newThread.start(); // �����߳�

	}

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// if (msg.what == UpdateTextView) {
			// showText.setText("sub thread update UI");// ���½�����ʾ
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
		
		//�ָ����ճ�����ʱ��sunrises��sundowns(�ַ�����ʽ)
		String[] suntimes = myWeather.suntime[0].split("\\|",2);
		String sunrises,sundowns;
		sunrises = suntimes[0];
		sundowns = suntimes[1];
		//ת��Ϊʱ���ʽ 
		DateFormat sundf = new SimpleDateFormat("HH:mm");
		Date sunrise = null;
		Date sundown = null;
		try {
			sunrise = sundf.parse(sunrises);
			sundown = sundf.parse(sundowns);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//�������ϵĲ�����ͬ��ͨ���ճ�����ʱ������ж����
		boolean flag1 = dt.after(sunrise);
		boolean flag2 = dt.before(sundown);
		if(flag1 && flag2){
			windD.setText(myWeather.windDD[0]);
			windP.setText(myWeather.windPD[0]);

			phenomena.setBackgroundDrawable(getResources().getDrawable(
					DWeatherArray[Integer.parseInt(myWeather.weatherD[0])]));

			weather_condition.setText(WeatherCondition[Integer
					.parseInt(myWeather.weatherD[0])]);
		}else {
			windD.setText(myWeather.windDN[0]);
			windP.setText(myWeather.windPN[0]);

			phenomena.setBackgroundDrawable(getResources().getDrawable(
					DWeatherArray[Integer.parseInt(myWeather.weatherN[0])]));

			weather_condition.setText(WeatherCondition[Integer
					.parseInt(myWeather.weatherN[0])]);
		}
	}

	public Weather getWeather(String strResult) {

		try {
			String suntime;
			// /����
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

				weather.suntime[i] = jsob.getString("fi");// �ճ�����ʱ�䣬һ�����ַ������������ָ�

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