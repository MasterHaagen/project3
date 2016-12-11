package com.nttu.csie.project3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String ITEM_DATE = "Item Date";
    private static final String ITEM_LOCATION = "Item Location";
    private static final String ITEM_ICON = "Item Icon";

    private GoogleMap mMap;

    // Google API用戶端物件
    private GoogleApiClient googleApiClient;

    // Location請求物件
    private LocationRequest locationRequest;

    // 記錄目前最新的位置
    private Location currentLocation;

    // 顯示目前與儲存位置的標記物件
    private Marker currentMarker, itemMarker;

    boolean move = true;

    private MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new MyDB(getApplicationContext());



        // 建立Google API用戶端物件
        configGoogleApiClient();

        // 建立Location請求物件
        configLocationRequest();

        // 連線到Google API用戶端
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    // ConnectionCallbacks
    @Override
    public void onConnected(Bundle bundle) {
        // 已經連線到Google Services
        // 啟動位置更新服務
        // 位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, MapsActivity.this);
    }

    // ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
    }

    // OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, R.string.google_play_service_missing,
                    Toast.LENGTH_LONG).show();
        }
    }

    // LocationListener
    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationLOG", "Update location - " + location.getLatitude() + ", " + location.getLongitude() + "]");
        // 位置改變
        // Location參數是目前的位置
        //currentLocation = location;
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());

        // 設定目前位置的標記
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        }
        else {
            currentMarker.setPosition(latLng);
        }

        // 移動地圖到目前的位置

        if(move){
            moveMap(latLng);
            move = false;
        }

        draw(location);
    }

    // 建立Google API用戶端物件
    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // 建立Location請求物件
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 連線到Google API用戶端
        if (!googleApiClient.isConnected() && currentMarker != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 移除位置請求服務
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 移除Google API用戶端連線
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // 建立位置的座標物件
        //LatLng place = new LatLng(25.033408, 121.564099);
        //LatLng place2 = new LatLng(22.748849, 121.147131);
        //addMarker(place2,"Title", "Data");
        //getDB();
        // 移動地圖
        //moveMap(place);

        // 加入地圖標記
        //addMarker(place, "Hello!", " Google Maps v2!");

        processController();
        //draw();
    }

    // 移動地圖到參數指定的位置
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // 在地圖加入指定位置與標題的標記
    private void addMarker(LatLng place, String title, String context) {
        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(context);
                //.icon(icon);

        // 加入並設定記事儲存的位置標記
        itemMarker = mMap.addMarker(markerOptions);
    }

    private void processController() {
        // 對話框按鈕事件
        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // 更新位置資訊
                            case DialogInterface.BUTTON_POSITIVE:
                                // 連線到Google API用戶端
                                if (!googleApiClient.isConnected()) {
                                    googleApiClient.connect();
                                }
                                break;
                            // 清除位置資訊
                            case DialogInterface.BUTTON_NEUTRAL:
                                Intent result = new Intent();
                                result.putExtra("lat", 0);
                                result.putExtra("lng", 0);
                                setResult(Activity.RESULT_OK, result);
                                finish();
                                break;
                            // 取消
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
        // 標記訊息框點擊事件
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // 如果是記事儲存的標記
                if (marker.equals(itemMarker)) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);

                    ab.setTitle(R.string.title_update_location)
                            .setMessage(R.string.message_update_location)
                            .setCancelable(true);

                    ab.setPositiveButton(R.string.update, listener);
                    ab.setNeutralButton(R.string.clear, listener);
                    ab.setNegativeButton(android.R.string.cancel, listener);

                    ab.show();
                }
            }
        });

        // 標記點擊事件
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 如果是目前位置標記
//                if (marker.equals(currentMarker)) {
//                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);
//
//                    ab.setTitle(R.string.title_current_location)
//                            .setMessage(R.string.message_current_location)
//                            .setCancelable(true);
//
//                    ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent result = new Intent();
//                            result.putExtra("lat", currentLocation.getLatitude());
//                            result.putExtra("lng", currentLocation.getLongitude());
//                            setResult(Activity.RESULT_OK, result);
//                            finish();
//                        }
//                    });
//                    ab.setNegativeButton(android.R.string.cancel, null);
//
//                    ab.show();
//
//                    return true;
//                }
//
                return false;
            }
        });
    }

//    private void getDB(){
//        //定義 ListView 每個 Item 的資料
//        List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
//        TypedArray regionIconList = getResources().obtainTypedArray(R.array.region_icon_list);
//        LatLng place2 = new LatLng(22.748849, 121.147131);
//        //addMarker(place2,"Title", "Data");
//
//        Cursor c = db.getFall();
//        int rows_num = c.getCount();
//        if(rows_num != 0) {
//            c.moveToFirst();			//將指標移至第一筆資料
//            for(int i=0; i<rows_num; i++) {
//                LatLng place = new LatLng(22.748849, 121.147131);
////                int id = c.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
////                String name = c.getString(1);
////                int value = c.getInt(2);
//                Calendar cal = Calendar.getInstance();
//                Date dt = new Date(c.getLong(1));
//                cal.setTime(dt);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
//                String str = df.format(cal.getTime());
//
//                Map<String, Object> item = new HashMap<String, Object>();
////                item.put(ITEM_DATE, str);
////                item.put(ITEM_LOCATION, c.getString(3));
////                //item.put(ITEM_ICON, regionIconList.getResourceId(i, 0));
////                item.put(ITEM_ICON, regionIconList.getResourceId(0, 0));
////                itemList.add(item);
//
//                c.moveToNext();		//將指標移至下一筆資料
//            }
//        }
//    }

    private void draw(Location location){
        // Instantiates a new Polyline object and adds points to define a rectangle
        if(currentLocation != null) {
            PolylineOptions rectOptions = new PolylineOptions()
                    .add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .add(new LatLng(location.getLatitude(), location.getLongitude()))
                    .width(8)
                    .color(Color.RED);
            //.geodesic(true);

            //.add(new LatLng(location.getLatitude(), location.getLongitude()))
            //.add(new LatLng(22.741132, 121.138855));

            // Get back the mutable Polyline
            Polyline polyline = mMap.addPolyline(rectOptions);
            //polyline.setPoints();

            //currentLocation = location;
        }
        currentLocation = location;
    }
}
