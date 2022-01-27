package com.trace;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.trace.data.Casino;
import com.trace.utils.DBOpenHelper;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Vibrator;

public class IndexActivity extends AppCompatActivity {

    private static final String TAG = "IndexActivity";
    private LocationManager locationManager;
    private String locationProvider;
    private DBOpenHelper dbOpenHelper;
    private Casino casino = null;
    private static final double EARTH_RADIUS = 6378.137;
    private String cur_patient;

    final int REQUEST_CODE_1 = 1;
    private String f="n";
	private Vibrator vibrator;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        dbOpenHelper = new DBOpenHelper(this);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);

        cur_patient = sp.getString("USERNAME", "");

        TextView tv_username = findViewById(R.id.tv_username);
        tv_username.setText("Welcome, " + cur_patient);

        casino = dbOpenHelper.getCasino("C0001");
        if(casino==null){
            Toast.makeText(IndexActivity.this,"The Clinician did not set up a casino!",Toast.LENGTH_LONG).show();
        }

        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        //从可用的位置提供器中，匹配以上标准的最佳提供器
        locationProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(IndexActivity.this,"No permission, please manually open the location permission",Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + ".." + Thread.currentThread().getName());
            //如果位置发生变化,重新显示
            showLocation(location);
        }
    };

    private void showLocation(Location location) {
        Log.d(TAG,"定位成功------->"+"location------>经度为：" + location.getLatitude() + "\n纬度为" + location.getLongitude());
        if(casino!=null){
             String lat = casino.getLat();
            String log = casino.getLog();

            double distince = DistanceOfTwoPoints(Double.parseDouble(lat),Double.parseDouble(log),location.getLatitude(),location.getLongitude());
            if(distince==0&&f.equals("n")){
                long[] pattern = { 200, 2000, 2000, 200, 200, 200 };
                vibrator.vibrate(pattern, -1);
                f="y";
                String action = "trace.mesAction";
                Intent n = new Intent(action);
                n.putExtra("pid", cur_patient);
                n.putExtra("flag","start");
                PackageManager packageManager = getBaseContext().getPackageManager();
                final Intent intent = new Intent(action);
                List<ResolveInfo> resolveInfo = packageManager
                        .queryIntentActivities(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo.size() > 0) {
                    System.out.println("find activity");
                }
                else{
                    System.out.println("no find activity");
                }
                startActivityForResult(n, REQUEST_CODE_1);

            }

            if(f.equals("y")&&distince>0){
                f="n";
                String action = "trace.mesAction";
                Intent n = new Intent(action);
                n.putExtra("pid", cur_patient);
                n.putExtra("flag","end");
                PackageManager packageManager = getBaseContext().getPackageManager();
                final Intent intent = new Intent(action);
                List<ResolveInfo> resolveInfo = packageManager
                        .queryIntentActivities(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo.size() > 0) {
                    System.out.println("find activity");
                }
                else{
                    System.out.println("no find activity");
                }
                startActivityForResult(n, REQUEST_CODE_1);
            }
        }
    }

    /**
     * 依据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离：单位为公里
     */
    public static double DistanceOfTwoPoints(double lat1,double lng1,
                                             double lat2,double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        Log.i("distince ",s+"");
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_out:
                startActivity(new Intent(IndexActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_1) {
            String res = data.getStringExtra("result");
            System.out.println("收到："+ res);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}