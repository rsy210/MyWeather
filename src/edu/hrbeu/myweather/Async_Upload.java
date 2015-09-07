package edu.hrbeu.myweather;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


public class Async_Upload extends AsyncTask<Void, Void, String> {

    private Context mContext;
    String filePath = null;

    public Async_Upload(Context listener,String filePath) {
    	super();
    	
    	mContext = listener;
        
        this.filePath = filePath;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
        	Log.i("ccc","poststarts1");
            HttpRequestManager post = new HttpRequestManager();
            post.setCharset(HTTP.UTF_8)
                    .setConnectionTimeout(5000)
                    .setSoTimeout(10000);
            final ContentType TEXT_PLAIN = ContentType.create("text/plain",
                    Charset.forName(HTTP.UTF_8));
            post.setOnHttpRequestListener(new HttpRequestManager.OnHttpRequestListener() {

                @Override
                public void onRequest(HttpRequestManager request) throws Exception {
                    //设置发送请求的header信息
                	Log.i("ccc","onRequest1");
                    //配置要POST的数据
                    MultipartEntityBuilder builder = request.getMultipartEntityBuilder();
                    builder.addTextBody("username","rsyben", TEXT_PLAIN);//中文

                    //附件部分
/*                    builder.addBinaryBody("headImg", new File(
                    		Environment.getExternalStorageDirectory()
            				+ "/Penguins.jpg"));*/
                    builder.addBinaryBody("headImg", new File(filePath));
                    Log.i("ccc","onRequest2");
                    request.buildPostEntity();
                    Log.i("ccc","onRequest3");
                }

                @Override
                public String onSucceed(int statusCode, HttpRequestManager request) throws Exception {
                    return request.getInputStream();
                }

                @Override
                public String onFailed(int statusCode, HttpRequestManager request) throws Exception {
                    return request.getInputStream();
                }
            });
            Log.i("ccc","poststarts2");
            //发起请求 
            String retSrc = post.post("http://192.168.100.107:8000/media/");
            Log.i("ccc","poststarts3");
            if (TextUtils.isEmpty(retSrc)) {
                return null;
            }
                return retSrc;
        } catch (Throwable e) {
            Log.i("ccc",e.toString());
        }
        return null;
    }
}