package com.nttu.csie.project3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import com.github.mikephil.charting.utils.Utils;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // 定位設備授權請求代碼
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private Toolbar toolbar;
    private ImageView img;
    //懸浮按鈕
    private FloatingActionButton fab;
    //左側選單
    private DrawerLayout drawer;
    //側滑動作
    private NavigationView navigationView;
    private MyDB db;
    private boolean test = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("行動開發");

        // initialize the utilities
        Utils.init(this);

        processViews();

        setSupportActionBar(toolbar);

        //右下角信箱按鈕觸發
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        if(test){
                            img.setImageResource(R.drawable.dark);
                            test = false;
                        } else {
                            img.setImageResource(R.drawable.light);
                            test = true;
                        }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        db = new MyDB(getApplicationContext());

        if (db.getCount() == 0) {
            db.sample();
        }

//        Date now = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(now);
        //cal.set(Calendar.HOUR_OF_DAY, 0);
        //cal.set(Calendar.MINUTE, 0);
        //cal.set(Calendar.SECOND, 0);
        //毫秒可根据系统需要清除或不清除
        //acal.set(Calendar.MILLISECOND, 0);
//        long startTime = cal.getTimeInMillis();
        //test.setText(""+startTime);
    }

    private void processViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        img = (ImageView)findViewById(R.id.imageView);
    }

    //Onclick調用
    public void onClick_Btn1(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,BarChart2Activity.class);
        startActivity(intent);
    }

    public void onClick_Btn2(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,BarChartActivity.class);
        startActivity(intent);
    }

    public void onClick_Btn3(View view) {
        // 讀取與處理定位設備授權請求
        requestLocationPermission();
    }

    public void onClick_Btn4(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,test3.class);
        startActivity(intent);
    }

    public void onClick_Btn5(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,mix.class);
        startActivity(intent);
    }

    // 讀取與處理定位設備授權請求
    private void requestLocationPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_PERMISSION);
            }
        }

        // 啟動地圖與定位元件
        processLocation();
    }

    // 啟動地圖與定位元件
    private void processLocation() {
        // 啟動地圖元件用的Intent物件
        Intent intentMap = new Intent(MainActivity.this, MapsActivity.class);

//        // 設定儲存的座標
//        intentMap.putExtra("lat", item.getLatitude());
//        intentMap.putExtra("lng", item.getLongitude());
//        intentMap.putExtra("title", item.getTitle());
//        intentMap.putExtra("datetime", item.getLocaleDatetime());

        // 啟動地圖元件
        startActivity(intentMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        // 如果是定位設備授權請求
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            // 如果在授權請求選擇「允許」
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 啟動地圖與定位元件
                processLocation();
            }
            // 如果在授權請求選擇「拒絕」
            else {
                // 顯示沒有授權的訊息
                Toast.makeText(this, R.string.write_external_storage_denied,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.history) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,BarChartActivity.class);
            startActivity(intent);
            //finish();
        }
        else if (id == R.id.icon_fall) {
            //FallTest
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,FallTest.class);
            startActivity(intent);

            //Fall
            //startService(new Intent(this, Fall.class));

        }
        else if (id == R.id.icon_step) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,StepTest.class);
            startActivity(intent);
        }
//          else if (id == R.id.nav_camera) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
