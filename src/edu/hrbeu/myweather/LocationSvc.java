package edu.hrbeu.myweather;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author SunnyCoffee
 * @date 2014-1-19
 * @version 1.0
 * @desc 定位服务
 * 
 */
public class LocationSvc extends Service implements LocationListener {

	private static final String TAG = "LocationSvc";
	private LocationManager locationManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "Get the current position111111111111111111 \n" );
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "Get the current position222222222222222222222222 \n" );
		if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) locationManager
				.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
						this);
		else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) locationManager
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						this);
		else Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "Get the current position \n" + location);

		//通知Activity
		Intent intent = new Intent();
		intent.setAction(Common.LOCATION_ACTION);
		intent.putExtra(Common.LOCATION, location.toString());
		sendBroadcast(intent);

		// 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
		locationManager.removeUpdates(this);
		stopSelf();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
