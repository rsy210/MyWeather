package edu.hrbeu.myweather;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public class SharewearActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharewear);
		String myurl = "http://192.168.100.107:8000/my/";
		String url = "http://192.168.100.107:8000/image/";
		String uploadFile = Environment.getExternalStorageDirectory()
				+ "/image.jpg";
		String newName = "image.jpg";
		uploadFile(url, uploadFile, newName);
	}

	public void uploadFile(final String imageurl, final String path,
			final String newName) {
		// 和GET方式一样，先将参数放入List

		final LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("name", "lqciszhu"));
		Thread newThread;
		newThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// String url = "http://127.0.0.1:8000/media/";
				try {
					// 创建一个默认的HttpClient
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(imageurl);
					httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
																					// //
																					// Entity中
					File file = new File(path);
					// 封装请求参数
					MultipartEntity mpEntity = new MultipartEntity();
					FileBody cbFile = new FileBody(file);
					mpEntity.addPart("userfile", cbFile); // <input type="file"
															// name="userfile"
															// /> 对
					httppost.setEntity(mpEntity);
					System.out.println("executing request "
							+ httppost.getRequestLine());

					HttpResponse response = httpclient.execute(httppost);
					HttpEntity resEntity = response.getEntity();

					System.out.println(response.getStatusLine());// 通信Ok
					String json = "";
					String path = "";
					if (resEntity != null) {
						// System.out.println(EntityUtils.toString(resEntity,"utf-8"));
						json = EntityUtils.toString(resEntity, "utf-8");
						JSONObject p = null;
						try {
							p = new JSONObject(json);
							path = (String) p.get("path");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (resEntity != null) {
						resEntity.consumeContent();
					}

					httpclient.getConnectionManager().shutdown();

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
			if (msg.what == 1) {
				// citytitle.setText(myWeather.city);
				// // nowtemp.setText(tempByBD + "°");
				//
				// changeview(view_today);
				// RefreshWeather(0);
				// changeview(view_tomorrow);
				// RefreshWeather(1);
				// changeview(view_afterday);
				// RefreshWeather(2);// 更新界面显示
			}
			if (msg.what == 2) {
				// RefreshIndex();
			}

			super.handleMessage(msg);

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sharewear, menu);
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
