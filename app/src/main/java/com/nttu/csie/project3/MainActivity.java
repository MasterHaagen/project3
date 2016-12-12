package com.nttu.csie.project3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//mix------------------------------
import android.content.Context;
import android.content.pm.ActivityInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {

    // 定位設備授權請求代碼
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private Toolbar toolbar;
    private ImageView img;
    private TextView username;
    private TextView userphone;
    private User user;
    private SharedPreferences prefs;
    //懸浮按鈕
    private FloatingActionButton fab;
    //左側選單
    private DrawerLayout drawer;
    //側滑動作
    private NavigationView navigationView;
    private View v;
    private MyDB db;
    static boolean  alert = true;

    MediaPlayer mediaPlayer01;

    private GmsLocationUtil mGmsLocationUtil;
    String sLocation = "0,0";

    //mix-------------------------------------------------------------------------------------------------
    //�^��-----------------------------------------------------------------------
    float Fmax;
    float Force;
    float totalForce = (float) 0.0;
    //    float x,y,z;
//    float gx,gy,gz;
//    float ggx,ggy,ggz;
//    float[] agy = new float[100];
//    float[] agz = new float[100];
    float sma;
    //    boolean check = false;
//    boolean check1 = false;
    boolean fall = false;
    int check_Time = -1;
    String gcom="no";
    TextView textView1;
    TextView textView2;
    TextView edit_test;
    TextView textView4;
    boolean isStarted;
    //    boolean mJustCall;
//    boolean checkCall;
    boolean move = false;
    double degree;
    int sensorTime = 0;
    //step--------------------------------------------------------------
	float[] fx = new float[1000];
	boolean xup=false;
	boolean xdown=false;
	float xmax = 0;
	float xmin = 0;
	float xTempMax = -99;
	float xTempMin = 99;
	boolean xChanged = false;
	boolean xToped = false;
	float xGateValue = 0;
	float[] xGvAverage = new float[10];
	int xGvCount = 0;
	float xBottomTime = -1;
	float xBottomTime2 = -1;
	float xTempButtonTime = 0;
	float xTop = 99;//
	float xBottom = 99;//
    Calendar start_time = Calendar.getInstance();
    Calendar end_time = Calendar.getInstance();
	//Y----------------------------------------
	float[] fy = new float[1000];
	boolean yup=false;
	boolean ydown=false;
	float ymax = 0;
	float ymin = 0;
	float yTempMax = -99;
	float yTempMin = 99;
	boolean yChanged = false;
	boolean yToped = false;
	float yGateValue = 0;
	float[] yGvAverage = new float[10];
	int yGvCount = 0;
	float yBottomTime = -1;
	float yBottomTime2 = -1;
	float yTempButtonTime = 0;
	float yTop = 99;//
	float yBottom = 99;//
	//common----------------
	int totalStep = 0;
	int effectiveStep = 0;
	boolean isStart = false;
	int timeNow = 0;
	int sensorCount = 0;
	String strx = "";
	String stry = "";
	String strz = "";
	String X = "";
	String GV = "";

	//new----------------------------------------------------------
	float gvxmax = 0;
	float gvxmin = 0;
	float[] xmaxAverage = new float[10];
	float[] xminAverage = new float[10];
	float gvymax = 0;
	float gvymin = 0;
	float[] ymaxAverage = new float[10];
	float[] yminAverage = new float[10];
	boolean xOneStep = false;
	boolean yOneStep = false;
	int endTime = -1000;
	int walkingTimeCount = 0;
	int startTime;

    private SensorManager smgr;
    private Sensor mAccelerometer;
    private List<Sensor> slist;
    private float[] arrayX = new float[1000];
    private float[] arrayY = new float[1000];
    private float[] arrayZ = new float[1000];
    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            //if(check_Time != sensorCount){
            //test.setText(user.username);
            if (event.sensor == slist.get(0)) {
                if (isStarted == false) return;
                if (timeNow == sensorTime) return;
                sensorTime = timeNow;
                if (sensorCount < 999)
                    sensorCount++;
                else
                    sensorCount = 0;
                fall = false;
                //�^��-------------------------------------------------------------------------------------------------------------
                totalForce = 0;
                sma = 0;
                arrayX[sensorCount] = event.values[0];
                arrayY[sensorCount] = event.values[1];
                arrayZ[sensorCount] = event.values[2];
                degree = Math.sin(event.values[0] / SensorManager.GRAVITY_EARTH);

                if (sensorCount >= 6) {
                    totalForce += Math.pow((float) (arrayX[sensorCount] + arrayX[sensorCount - 1] - arrayX[sensorCount - 2] - arrayX[sensorCount - 3]) / 4 / SensorManager.GRAVITY_EARTH, 2.0);//X
                    totalForce += Math.pow((float) (arrayY[sensorCount] + arrayY[sensorCount - 1] - arrayY[sensorCount - 2] - arrayY[sensorCount - 3]) / 4 / SensorManager.GRAVITY_EARTH, 2.0);//Y
                    totalForce += Math.pow((float) (arrayZ[sensorCount] + arrayZ[sensorCount - 1] - arrayZ[sensorCount - 2] - arrayZ[sensorCount - 3]) / 4 / SensorManager.GRAVITY_EARTH, 2.0);//Z
                    totalForce = (float) Math.sqrt(totalForce);
                    if (totalForce > Fmax) {
                        Fmax = totalForce;
                    }

                    //fmax值
                    if (totalForce > 1.5) {

                        //if(!gcom.equals("NO"))
                        move = true;

							/*gx= arrayX[sensorCount]-arrayX[sensorCount-5];
							gy = arrayY[sensorCount]-arrayY[sensorCount-5];
							gz= arrayZ[sensorCount]-arrayZ[sensorCount-5];//1S
							ggx= arrayX[sensorCount]-arrayX[sensorCount-1];//0.2S
							ggy= arrayY[sensorCount]-arrayY[sensorCount-1];
							ggz= arrayZ[sensorCount]-arrayZ[sensorCount-1];
							textView2.setText(""+"gx: "+gx+" "+"gy: "+gy+" "+"gz: "+" "+gz);
							textView3.setText(""+"ggx: "+ggx+" "+"ggy: "+ggy+" "+"ggz: "+" "+ggz);*/

                            check_Time = sensorCount;

                    }

                }else;
                if (move) {
                    if (sensorCount > check_Time + 2) {//0.2嚙踝蕭X嚙踝蕭
                        if (Math.abs(degree) <= 0.6) {
                            gcom = YZcompare();
                            if (gcom != "NO") fall = true;
                            //textView2.setText("Fmax: " + Fmax+"\n"+"\n"+gcom+"\n"+"\n"+degree+"\n"+"\n");
                            /**
                             * 警告視窗
                             **/
                            processFall();
                            /**
                             **/
                            move = false;
                            Fmax = 0;
                        } else {
                            move = false;
                        }
                        //check_Time = -1;
                    }else;
                }else;
                if (fall) return;
                //step------------------------------------------------------------------------------------------------------------------

                if(sensorCount >= 4){//��閬�4蝑誑銝��������
        			fx[sensorCount] = filter(arrayX[sensorCount], arrayX[sensorCount-1], arrayX[sensorCount-2], arrayX[sensorCount-3]);
        			fy[sensorCount] = filter(arrayY[sensorCount], arrayY[sensorCount-1], arrayY[sensorCount-2], arrayY[sensorCount-3]);
        			if(sensorCount >= 5){
        				if(XStepCounter() || YStepCounter()){
                            totalStep++;
                            //Toast.makeText(getApplicationContext(), totalStep, Toast.LENGTH_SHORT).show();
                            edit_test.setText(totalStep+"");

                            //Log.d("testCOUNT", totalStep+"");
                            if(endTime==-1000){
                                startTime = timeNow;
                                start_time = Calendar.getInstance();
                            }else;
        					endTime = timeNow;

        				}
        			}else;
        		}else;
            }

        }

        //嚙踝蕭T嚙論改蕭嚙豌會嚙瘢嚙編
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private List<Sensor> lightSensor;
    private Handler mHandlerTime = new Handler();
    private final Runnable timerRun = new Runnable(){
	    public void run(){
		  mHandlerTime.postDelayed(this, 100);
		  if(timeNow < 2000000000){
              timeNow++;
		  } else{
              timeNow = 0;
			xBottomTime = -1;
			xBottomTime2 = -1;
			yBottomTime = -1;
			yBottomTime2 = -1;
			endTime = -1000;
		  }
		  if(timeNow - endTime < 100){
			  walkingTimeCount++;
		  }else{
			  end_time = Calendar.getInstance();
              if(totalStep!=0){
                  Item item = new Item(0,start_time.getTimeInMillis(),0,end_time.getTimeInMillis(),totalStep);
                  db.insert(item);
              }
			  endTime = -1000;
			  totalStep=0;
		  }

		}
	};
    View.OnClickListener start_1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            img.setImageResource(R.drawable.light);
            if (isStarted) {
                smgr.unregisterListener(mListener);
                mHandlerTime.removeCallbacks(timerRun);
                isStarted = false;
            } else {
                //textView1.setText(" ");
                isStarted = true;
                check_Time = -1;
                Fmax = (float) -10.0;
                Force = (float) 0.0;
                arrayX = new float[1000];
                arrayY = new float[1000];
                arrayZ = new float[1000];
                timeNow = 0;
                sensorTime = 0;
                fall = false;
                //step--------------------------
                smgr.registerListener(mListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        		xup = false;
        		xdown = false;
        		xmax = 0;
        		xmin = 0;
        		fx = new float[1000];
        		xTempMax = -99;
        		xTempMin = 99;
        		xGateValue = 0;
        		xGvAverage = new float[10];
        		xGvCount = 0;
        		xBottomTime = -1;
        		xBottomTime2 = -1;
        		strx = "";
        		stry = "";
        		strz = "";
        		isStart = true;
        		//Y-------------------------------
        		fy = new float[1000];
        		yup = false;
        		ydown = false;
        		ymax = 0;
        		ymin = 0;
        		yTempMax = -99;
        		yTempMin = 99;
        		yChanged = false;
        		yToped = false;
        		yGateValue = 0;
        		yGvAverage = new float[10];
        		yGvCount = 0;
        		yBottomTime = -1;
        		yBottomTime2 = -1;
        		yTempButtonTime = 0;
        		yTop = 99;//
        		yBottom = 99;//
        		//common---------------------------
        		totalStep = 0;
        		effectiveStep = 0;
        		timeNow = 0;
        		sensorCount = 0;
        		endTime = -1000;
        		walkingTimeCount = 0;
        		//new----------------------------
        		xmaxAverage = new float[10];
        		xminAverage = new float[10];
        		gvymax = 0;
        		gvymin = 0;
        		ymaxAverage = new float[10];
        		yminAverage = new float[10];
        		xOneStep = false;
        		yOneStep = false;
        		xTop = 99;
        		xBottom = 99;
        		gvxmax = 0;
        		gvxmin = 0;
        		timerRun.run();
            }
        }
    };

    //mix--------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_name);

        // initialize the utilities
        Utils.init(this);

        processViews();

        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //右下角信箱按鈕觸發
        fab.setOnClickListener(start_1);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                        if(test){
//                            img.setImageResource(R.drawable.dark);
//                            test = false;
//                        } else {
//                            img.setImageResource(R.drawable.light);
//                            test = true;
//                        }
//            }
//        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        db = new MyDB(getApplicationContext());

        if (db.getCount() == 0) {
            db.sample();
        }

        //mix-----------------------------------------------------------------------------------------------------------------------
        //Button start = (Button)findViewById(R.id.bt1);
        //start.setOnClickListener(start_1);

        //�T�b�[�t��
        smgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        slist = smgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        mAccelerometer = smgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //���P��
        lightSensor = smgr.getSensorList(Sensor.TYPE_LIGHT);

        isStarted = false;

        mGmsLocationUtil = new GmsLocationUtil(this);
        mGmsLocationUtil.connect();
        mGmsLocationUtil.startUpdateLocation(this);

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

    @Override
    public void onDestroy() {
        smgr.unregisterListener(mListener);
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        username.setText(prefs.getString("NAME", "使用者"));
        userphone.setText("緊急聯絡人電話" + prefs.getString("PHONE", "0900000000"));
    }

    private void processViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        v = navigationView.getHeaderView(0);
        img = (ImageView) findViewById(R.id.imageView);
        //textView1 = (TextView) findViewById(R.id.tv1);
        //textView2 = (TextView) findViewById(R.id.tv2);
        edit_test = (TextView) findViewById(R.id.edit_test);
        //textView4 = (TextView) findViewById(R.id.tv4);
        username = (TextView) v.findViewById(R.id.user_name);
        userphone = (TextView)v.findViewById(R.id.user_phone);

        prefs = getSharedPreferences("DATA", MODE_PRIVATE);
        username.setText(prefs.getString("NAME", "使用者"));
        userphone.setText(prefs.getString("PHONE", "0900000000"));
        //userphone = prefs.getString("PHONE", "0900000000");
    }

    private void processFall() {
        img.setImageResource(R.drawable.fall);
        Calendar c = Calendar.getInstance();
        db.insert2(c.getTimeInMillis(), gcom, sLocation);

        //音效
        mediaPlayer01 = MediaPlayer.create(MainActivity.this, R.raw.alert);
        mediaPlayer01.setLooping(true);
        mediaPlayer01.start();

        //警告視窗
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("是否取消警報?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        img.setImageResource(R.drawable.light);
                        //alert = false;
                        mediaPlayer01.stop();
                    }
                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //finish();
//                    }
//                })
                .show();
        if(alert){
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            String str = df.format(c.getTime());

            SmsManager smsManager = SmsManager.getDefault();
            String to = userphone.getText().toString();
            String content = str + " 跌倒偵測 \n" + sLocation;
            try {
                smsManager.sendTextMessage(to.toString(), null, content.toString(), PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        //簡訊
//        if(alert){
//            SmsManager smsManager = SmsManager.getDefault();
//            String to = "5555";
//            String content = "SMS test";
//            try {
//                smsManager.sendTextMessage(to.toString(), null, content.toString(), PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0), null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        //String phone = "0909084003";
        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        //startActivity(intent);
//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this,test3.class);
//        startActivity(intent);
    }

    //Onclick調用
    public void onClick_Btn1(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,BarChartActivity.class);
        startActivity(intent);
    }

    public void onClick_Btn2(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,BarChart2Activity.class);
        startActivity(intent);
    }

    public void onClick_Btn3(View view) {
        // 讀取與處理定位設備授權請求
        requestLocationPermission();
    }

    public void onClick_Btn4(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,FallActivity.class);
        startActivity(intent);
    }

    public void onClick_Btn5(View view) {
        img.setImageResource(R.drawable.fall);
        Calendar c = Calendar.getInstance();
        db.insert2(c.getTimeInMillis(), gcom, sLocation);

        //音效
        mediaPlayer01 = MediaPlayer.create(MainActivity.this, R.raw.alert);
        mediaPlayer01.setLooping(true);
        mediaPlayer01.start();

        //警告視窗
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("是否取消警報?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        img.setImageResource(R.drawable.light);
                        //alert = false;
                        mediaPlayer01.stop();
                    }
                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //finish();
//                    }
//                })
                .show();
        if(alert){
            SmsManager smsManager = SmsManager.getDefault();
            String to = "5555";
            String content = "2016/12/10 11:04 跌倒偵測警告";
            try {
                smsManager.sendTextMessage(to.toString(), null, content.toString(), PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        //簡訊
//        if(alert){
//            SmsManager smsManager = SmsManager.getDefault();
//            String to = "5555";
//            String content = "SMS test";
//            try {
//                smsManager.sendTextMessage(to.toString(), null, content.toString(), PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0), null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        //String phone = "0909084003";
        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        //startActivity(intent);
//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this,test3.class);
//        startActivity(intent);
    }

    public void onClick_Btn6(View view){

    }

    //接收子模塊數據
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            }
        }
    }

    public void delay(int ms){
        try{
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d("TAG", "Update location - " + location.getLatitude() + ", " + location.getLongitude() + "]");
        sLocation = location.getLatitude() + ", " + location.getLongitude();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

        if (id == R.id.nav_manage) {
            startActivity(new Intent(this,Setting.class));
            //finish();
        }
//        else if (id == R.id.icon_fall) {
//            //FallTest
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this,FallTest.class);
//            startActivity(intent);
//
//            //Fall
//            //startService(new Intent(this, Fall.class));
//
//        }
//        else if (id == R.id.icon_step) {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this,StepTest.class);
//            startActivity(intent);
//        }
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

    //mix-----------------------------------------------------------------------------------------
    void Xup_and_down(float a, float b){
		float delta = a - b;
		if(delta>=0){
			xup=true;
			Xgetpoint(true, b);
		}
		else{
			xdown=true;
			Xgetpoint(false, b);
		}
	}
    void Yup_and_down(float a, float b){
		float delta = a - b;
		if(delta>=0){
			yup=true;
			Ygetpoint(true, b);
		}
		else{
			ydown=true;
			Ygetpoint(false, b);
		}
	}

	public boolean XStepCounter(){
		xOneStep = false;
		Xup_and_down(arrayX[sensorCount], arrayX[sensorCount-1]);
		if(xTop - xBottom > 1){
			if(timeNow % 20 ==0){//每秒取一次最大最小值
				xmax = xTempMax;
				xmin = xTempMin;
				xTempMax = -99;
				xTempMin = 99;
				xChanged = true;
			}
		}else{
			xTempMax = -99;
			xTempMin = 99;
		}
		if(xmax - xmin > 1){
			if(xChanged){//每秒取一次閥值
				int gvDiv = 0;
				xGateValue = (xmax+xmin)/2;
				xGvAverage[xGvCount] = xGateValue;
				xmaxAverage[xGvCount] = xmax;
				xminAverage[xGvCount] = xmin;
				if(xGvCount < 2)
					xGvCount++;
				else
					xGvCount = 0;
				xGateValue = 0;
				gvxmax = 0;
				gvxmin = 0;
				for(int i = 0; i<3; i++){
					if (xGvAverage[i] != 0.0)
						xGateValue += xGvAverage[i];
					else {
						gvDiv = i;
						break;
					}
					if(i == 2) gvDiv = 3;
				}
				for(int i = 0; i<3; i++){
					if (xmaxAverage[i] != 0.0)
						gvxmax += xmaxAverage[i];
					else
						break;
				}
				for(int i = 0; i<3; i++){
					if (xminAverage[i] != 0.0)
						gvxmin += xminAverage[i];
					else
						break;
				}
				xGateValue  = xGateValue / gvDiv;
				gvxmax  = gvxmax / gvDiv;
				gvxmin  = gvxmin /gvDiv;
				X += "t1+";
				GV += xGateValue+"+";

				//timeCount = timeNow;
				xChanged = false;
			}else;
			if(xTop > xGateValue && xTop != 99)	{
				xToped = true;
				xTop = 99;
			}
			if(xToped){
				if(xBottom < (2*xGateValue+gvxmin)/3 && xBottom != 99){
					if(xBottomTime == -1)
						xBottomTime = xTempButtonTime;
					else
						xBottomTime2 = xTempButtonTime;
					xBottom = 99;
					xToped = false;
				}
			}else;
			if (xBottomTime2 != -1){

				if (xBottomTime2 - xBottomTime >= 2 && xBottomTime2 - xBottomTime <= 20){
					xOneStep = true;
					xBottomTime = xBottomTime2;
				} else if (xBottomTime2 - xBottomTime < 2) {
					//walking = false;
					//effectiveStep = 0;
					X="";
					GV="";

				} else if (xBottomTime2 - xBottomTime > 20){
					X="";
					GV="";
					xBottomTime = xBottomTime2;
				} else {
					X="";
					GV="";
					xBottomTime = xBottomTime2;
				}

	 			 xBottomTime2 = -1;
			}else;
		}else;
		return xOneStep;
	}

	public boolean YStepCounter(){
		yOneStep = false;
		Yup_and_down(arrayY[sensorCount], arrayY[sensorCount-1]);
		if(yTop - yBottom > 1){
			if(timeNow % 20 ==0){//每秒取一次最大最小值
				ymax = yTempMax;
				ymin = yTempMin;
				yTempMax = -99;
				yTempMin = 99;
				yChanged = true;
			}
		}else;
		if(ymax - ymin > 1){
			if(yChanged){//每秒取一次閥值
				int gvDiv = 0;
				yGateValue = (ymax+ymin)/2;
				yGvAverage[yGvCount] = yGateValue;
				ymaxAverage[yGvCount] = ymax;
				yminAverage[yGvCount] = ymin;
				if(yGvCount < 2)
					yGvCount++;
				else
					yGvCount = 0;
				yGateValue = 0;
				gvymax = 0;
				gvymin = 0;
				for(int i = 0; i<3; i++){
					if (yGvAverage[i] != 0.0)
						yGateValue += yGvAverage[i];
					else {
						gvDiv = i;
						break;
					}
					if(i == 2) gvDiv = 3;
				}
				for(int i = 0; i<3; i++){
					if (ymaxAverage[i] != 0.0)
						gvymax += ymaxAverage[i];
					else
						break;
				}
				for(int i = 0; i<3; i++){
					if (yminAverage[i] != 0.0)
						gvymin += yminAverage[i];
					else
						break;
				}
				yGateValue  = yGateValue / gvDiv;
				gvymax  = gvymax / gvDiv;
				gvymin  = gvymin /gvDiv;
				yChanged = false;
			}else;
			if(yTop > yGateValue && yTop != 99)	{
				yToped = true;
				yTop = 99;
			}
			if(yToped){
				if(yBottom < (2*yGateValue+gvymin)/3 && yBottom != 99){
					if(yBottomTime == -1)
						yBottomTime = yTempButtonTime;
					else
						yBottomTime2 = yTempButtonTime;
					yBottom = 99;
					yToped = false;
				}
			}else;
			if (yBottomTime2 != -1){

				if (yBottomTime2 - yBottomTime >= 2 && yBottomTime2 - yBottomTime <= 20){
					yOneStep = true;
					yBottomTime = yBottomTime2;
				} else if (yBottomTime2 - yBottomTime < 2) {
					//walking = false;
					//effectiveStep = 0;
				} else if (yBottomTime2 - yBottomTime > 20){
					yBottomTime = yBottomTime2;
				} else {
					yBottomTime = yBottomTime2;
				}

	 			     yBottomTime2 = -1;
			}else;
		}else;
		return yOneStep;
	}


    void Xgetpoint(boolean status, float b){
		if(status){
			if(xdown){
				xdown=false;
				xBottom =b;
				xTempButtonTime = timeNow;
				if(b < xTempMin)
					xTempMin = b;
			}
		}
		else {
			if(xup){
				xup=false;
				xTop = b;
				if(b > xTempMax)
					xTempMax = b;
			}
		}
	}

    void Ygetpoint(boolean status, float b){
		if(status){
			if(ydown){
				ydown=false;
				yBottom =b;
				yTempButtonTime = timeNow;
				if(b < yTempMin)
					yTempMin = b;
			}
		}
		else {
			if(yup){
				yup=false;
				yTop = b;
				if(b > yTempMax)
					yTempMax = b;
			}
		}
	}

    float filter(float now, float last1, float last2, float last3){
        return (now+last1+last2+last3)/4;
    }

    public String YZcompare(/*float cx,float cy,float cz*/){
        if (Math.abs(arrayY[check_Time + 2]) < 4 && Math.abs(arrayZ[check_Time + 2]) < 4)//2.75
            return "NO";
        else{
            if(Math.abs(arrayY[check_Time + 2]) >= Math.abs(arrayZ[check_Time + 2])){
                switch (XYcompare(arrayX[sensorCount], arrayY[sensorCount])){
                    case 'P':
                        return "BW";
                    case 'M':
                        return "NO";
                    case 'N':
                        return "FW";
                }
            }
            else{
                switch (XZcompare(arrayX[check_Time + 2], arrayZ[check_Time + 2])){
                    case 'P':
                        return "RT";
                    case 'M':
                        return "NO";
                    case 'N':
                        return "LT";
                }
            }
        }
        return "out error";
    }


	public char XZcompare(float xa, float b){//4.5
        if (arrayX[sensorCount - 10]>0){
            if(5.4 <= b) return 'P';
                //else if(b<2.75)return 'M';
            else if(0 <= b && b < 5.4){
                if(Math.abs(xa) >= 3.5)
                    return 'P';
                else
                    return 'M';
            }
            else if (-5.4 < b && b < 0){
                if(Math.abs(xa) >= 3.5)
                    return 'N';
                else
                    return 'M';
            }
            else return 'N';
        }
        else{
            if(5.4 <= b) return 'N';
            else if(0 <= b && b < 4.5){
                if(Math.abs(xa) >= 3.5)
                    return 'N';
                else
                    return 'M';
            }
            else if (-5.4 < b && b < 0){
                if(Math.abs(xa) >= 3.5)
                    return 'P';
                else
                    return 'M';
            }
            else return 'P';
        }
    }
    public char XYcompare(float xa, float b){

        if(5.4 <= b) return 'P';
        else if(0 <= b && b < 5.4){
            if(Math.abs(xa) >= 3.5)
                return 'P';
            else
                return 'M';
        }
        else if (-5.4 < b && b < 0){
            if(Math.abs(xa) >= 3.5)
                return 'N';
            else
                return 'M';
        }
        else return 'N';
    }

}
