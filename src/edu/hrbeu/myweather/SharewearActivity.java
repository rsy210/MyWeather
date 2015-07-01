package edu.hrbeu.myweather;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SharewearActivity extends Activity {

	 String filePath = "C:/Users/Public/Pictures/Sample Pictures/考拉.jpg";// 测试写的文件路径，转换成自己的文件路径 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharewear);
		String myurl = "http://127.0.0.1:8000/media/";
		String uploadFile = Environment.getExternalStorageDirectory()+"/Penguins.jpg";
		String newName = "rsy";
		uploadFile(myurl,uploadFile,newName);
	}
	
	
	public void uploadFile(String actionUrl,String uploadFile,String newName)
    {
      String end ="\r\n";
      String twoHyphens ="--";
      String boundary ="*****";
      
      try
      {
    	//创建URL对象
        URL url =new URL(actionUrl);
        //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* 设置传送的method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        /* 设置DataOutputStream */
        DataOutputStream ds =
          new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; "+
                      "name=\"file1\";filename=\""+
                      newName +"\""+ end);
        ds.writeBytes(end);  
        /* 取得文件的FileInputStream */
        FileInputStream fStream =new FileInputStream(uploadFile);
        /* 设置每次写入1024bytes */
        int bufferSize =1024;
        byte[] buffer =new byte[bufferSize];
        int length =-1;
        /* 从文件读取数据至缓冲区 */
        while((length = fStream.read(buffer)) !=-1)
        {
          /* 将资料写入DataOutputStream中 */
          ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
        fStream.close();
        ds.flush();
        /* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 )
        {
          b.append( (char)ch );
        }
        /* 将Response显示于Dialog */
      //  showDialog("上传成功"+b.toString().trim());
        /* 关闭DataOutputStream */
        ds.close();
      }
      catch(Exception e)
      {
    //    showDialog("上传失败"+e);
      }
    }
	
	
	
	
	/*public void getWeatherDate(final String url, final int num,
			final String city, final String citycode) {
		Thread newThread; // 声明一个子线程
		Log.i("BBBB", "citycode:" + citycode);
		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作

				Log.i("CCCC", "citycode:" + citycode);

				String uri = "http://127.0.0.1:8000/media/";
				try {
					// 创建一个默认的HttpClient
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);
					
					 final File imageFile = new File(filePath); 
					  MultipartEntity multipartEntity = new MultipartEntity();
					 
					 JSONObject obj = new JSONObject();  
					    obj.put("username", "rsy");  
					    obj.put("headImg", "your parentid");  
					    httppost.setEntity(new StringEntity(obj.toString()));     
					    HttpResponse response;  
					    response = httpclient.execute(httppost);  
					    //检验状态码，如果成功接收数据   
					    int code = response.getStatusLine().getStatusCode();  
					    if (code == 200) {   
					        String rev = EntityUtils.toString(response.getEntity());//返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}          
					        obj = new JSONObject(rev);  
					        String id = obj.getString("username");  
					        String version = obj.getString("headImg");  
					// 创建一个GET请求
					HttpGet request = new HttpGet(url);
					Log.v("response text", url);
					// 发送GET请求，并将响应内容转换成字符串
					String response = httpclient.execute(request,
							new BasicResponseHandler());
					Log.v("response text", response);

					if (num == 1) {
						myWeather = getWeather(response);
					} else {
						myIndex = getIndex(response);
					}

					Cursor wCursor = wDataCache.getmyWeatherDB(citycode);
					if (wCursor == null || wCursor.getCount() <= 0) {
						wDataCache.insertmyWeatherDB(city, citycode,
								weatherResponse, indexResponse);
					} else {
						wDataCache.updatemyWeatherDB(city, citycode,
								weatherResponse, indexResponse);
					}

					Message m = new Message();
					m.what = num;
					myHandler.sendMessage(m);// 发送消息:系统会自动调用handleMessage方法来处理消息

				}} catch (ClientProtocolException e) {
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
	}*/
	
	
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
//				citytitle.setText(myWeather.city);
//				// nowtemp.setText(tempByBD + "°");
//
//				changeview(view_today);
//				RefreshWeather(0);
//				changeview(view_tomorrow);
//				RefreshWeather(1);
//				changeview(view_afterday);
//				RefreshWeather(2);// 更新界面显示
			}
			if (msg.what == 2) {
//				RefreshIndex();
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
