package edu.hrbeu.myweather;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoPreActivity extends Activity {

	
	public static String picPath ;
    private static final String TAG = "PhotoPreActivity";
 
    public static final String KEY_PHOTO_PATH = "photo_path";
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int TO_SELECT_PHOTO = 3;  
    private ProgressDialog progressDialog;
    private Button photo_back;
    private Button photo_upload;
    private Button photo_cancel;
    private ImageView photo_view1;
    private Bitmap bm;
	private String path;
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pre);
 
        photo_back = (Button) this.findViewById(R.id.photo_back);
        photo_upload = (Button) this.findViewById(R.id.photo_upload);
        photo_cancel = (Button) this.findViewById(R.id.photo_cancel);
        photo_view1 = (ImageView) this.findViewById(R.id.photo_view1);
        progressDialog = new ProgressDialog(this);
 
        photo_back.setOnClickListener(new ThisOnClickListener(0));
        photo_upload.setOnClickListener(new ThisOnClickListener(1));
        photo_cancel.setOnClickListener(new ThisOnClickListener(2));
 
        Bundle bundle = getIntent().getExtras();
         path = bundle.getString(KEY_PHOTO_PATH);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bm = BitmapFactory.decodeFile(path);
        photo_view1.setImageBitmap(bm);
        

    }
 
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//    	Log.i("pppp", "p=" + picPath);
//            if (resultCode==Activity.RESULT_OK ) {
//            	photo_view1.setImageBitmap(null);
//                picPath = data.getStringExtra(SharewearActivity.SAVED_IMAGE_DIR_PATH);
//                Log.i("pppp", "最终选择的图片=" + picPath);
//                Bitmap bm = BitmapFactory.decodeFile(picPath);
//                photo_view1.setImageBitmap(bm);
//            } else if (requestCode == 2) {
// 
//                try {
//                    Bitmap bm = null;
//                    ContentResolver resolver = getContentResolver();
//                    Uri uri = data.getData();
//                    bm = MediaStore.Images.Media.getBitmap(resolver, uri);
// 
//                    String[] pro = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = managedQuery(uri, pro, null, null, null);
//                    int column_index = cursor
//                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//                    String picPath1 = cursor.getString(column_index);
//                    Bitmap bitmap = BitmapFactory.decodeFile(picPath1);
//                    photo_view1.setImageBitmap(bitmap);
//                    cursor.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, e.toString());
//                }
//            }
//        
//        super.onActivityResult(requestCode, resultCode, data);
//    }
 
    public class ThisOnClickListener implements OnClickListener {
        private int index = 0;
 
        private ThisOnClickListener(int i) {
            index = i;
        }
 
        @Override
        public void onClick(View v) {
            switch (index) {
            case 0:
                Intent intent = new Intent(PhotoPreActivity.this, MainActivity.class);
                startActivity(intent); 
                break;
            case 1:
//            	Intent intent1 = new Intent(PhotoPreActivity.this, SharewearActivity.class);
//                startActivityForResult(intent1, TO_SELECT_PHOTO);
            	Async_Upload au= new Async_Upload(PhotoPreActivity.this,path);
        		au.execute();
        		Log.i("pppp", "最终选择的图片1=" + picPath);
            case 2:
                Intent intent2 = new Intent(PhotoPreActivity.this, SharewearActivity.class);
                startActivity(intent2);
                break;
            }
 
        }
 
    }
 
    private void postFile() throws Exception {
        progressDialog.setMessage("正在上传文件...");
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
 
        HttpPost httppost = new HttpPost("http://localhost:9002/upload.php");
        File file = new File("c:/TRASH/zaba_1.jpg");
 
        FileEntity reqEntity = new FileEntity(file, "binary/octet-stream");
 
        httppost.setEntity(reqEntity);
        reqEntity.setContentType("binary/octet-stream");
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
 
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
 
        httpclient.getConnectionManager().shutdown();
    }
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_pre, menu);
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
