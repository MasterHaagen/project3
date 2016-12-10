package com.nttu.csie.project3;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FallTest extends Activity {

    float max;
    float Force;
    float totalForce = (float)0.0;
    float x,y,z;
    float gx,gy,gz;
    float[] agy = new float[100];
    float[] agz = new float[100];
    boolean check = false;
    boolean check1 = false;
    int check_Time;
    String gcom;
    TextView textView1;
    boolean isStarted;
    boolean mJustCall;
    boolean checkCall;
    private SensorManager smgr;
    private List<Sensor> slist;
    private float[] arrayX= new float[100];
    private float[] arrayY= new float[100];
    private float[] arrayZ= new float[100];
    private List<Sensor> lightSensor ;
    private int m_nTime = 0;
    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event){
            // TODO Auto-generated method stub
            if (event.sensor == slist.get(0)) {
                if (isStarted == false) return;
                if(m_nTime%100==0){
                    m_nTime=0;
                }
                totalForce = 0;
                arrayX[m_nTime] = event.values[0];
                arrayY[m_nTime] = event.values[1];
                arrayZ[m_nTime] = event.values[2];
				/*x=event.values[0];
				y=event.values[1];
				z=event.values[2];*/
                if(m_nTime>=4){
                    totalForce += (float)Math.pow((arrayX[m_nTime]+arrayX[m_nTime-1]-arrayX[m_nTime-2]-arrayX[m_nTime-3])/4/SensorManager.GRAVITY_EARTH, 2.0);//X
                    totalForce += (float)Math.pow((arrayY[m_nTime]+arrayY[m_nTime-1]-arrayY[m_nTime-2]-arrayY[m_nTime-3])/4/SensorManager.GRAVITY_EARTH, 2.0);//Y
                    totalForce += (float)Math.pow((arrayZ[m_nTime]+arrayZ[m_nTime-1]-arrayZ[m_nTime-2]-arrayZ[m_nTime-3])/4/SensorManager.GRAVITY_EARTH, 2.0);//Z
                    totalForce = (float)Math.sqrt(totalForce);
                    if (totalForce > max){
                        max = totalForce;
                    }
				/*	gy = arrayY[m_nTime]-arrayY[m_nTime-1];
					agy[m_nTime] = gy;
					gz= arrayZ[m_nTime]-arrayZ[m_nTime-1];
					agz[m_nTime] = gz;*/
                }
            }
            else if(event.sensor==lightSensor.get(0)){
                Force=event.values[0];
            }
        }

        //精確度改變會呼叫
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
    private Handler mHandlerTime = new Handler();
    OnClickListener start_l = new OnClickListener(){
        @Override
        public void onClick(View v){
            if(isStarted == true) return;
            isStarted = true;

            max = (float)-10.0;
            Force=(float)0.0;
            textView1.setText("");
            smgr.registerListener(mListener,slist.get(0), SensorManager.SENSOR_DELAY_UI);
            timerRun.run();
            smgr.registerListener(mListener, lightSensor.get(0),SensorManager.SENSOR_DELAY_UI);

        }
    };
    private final Runnable timerRun = new Runnable(){
        public void run(){
            mHandlerTime.postDelayed(this, 200);
            m_nTime++; // 經過的秒數 + 1
            if(max > 2){
                if(!check){
				   	/*gx = arrayX[m_nTime-1]-arrayX[m_nTime-2];
				    gy = arrayY[m_nTime-1]-arrayY[m_nTime-2];
				    gz = arrayZ[m_nTime-1]-arrayZ[m_nTime-2];*/
                    check = true;
                    check_Time = m_nTime;
                }
                else if (check && m_nTime == check_Time+5){
                    gcom = YZcompare();
				   	/*String strgy = "";
				   	String strgz = "";
				   	for(int i=check_Time-5;i<check_Time+5;i++){
				   	strgy += " + "+agy[i];
				   	strgz += " + "+agz[i];
				   	}*/
                    if(gcom!="NO"){
                        textView1.setText("Max: " + max+"\n"+"\n"+gcom);
                        new AlertDialog.Builder(FallTest.this)
                                .setTitle("確認")
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage("是否取消警報?")
                                .setPositiveButton("是",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        textView1.setText("1");
                                        smgr.unregisterListener(mListener, slist.get(0));//當程式進入背景或結束時就不會讀取感測器了
                                        smgr.unregisterListener(mListener, lightSensor.get(0));
                                        isStarted = false;
                                        max=0;
                                        if(m_nTime >= check_Time+10){
                                            smgr.registerListener(mListener,slist.get(0), SensorManager.SENSOR_DELAY_UI);
                                            smgr.registerListener(mListener, lightSensor.get(0),SensorManager.SENSOR_DELAY_UI);
                                            m_nTime=0;
                                            check = false;
                                            isStarted = true;
                                        }
                                    }
                                })
                                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        finish();
                                    }
                                })
                                .show();
                    }
                    else if(gcom=="NO"){
                        textView1.setText("2"+"  "+max);
                        max=0;
                        m_nTime=0;
                        check = false;
				   		/*smgr.unregisterListener(mListener, slist.get(0));//當程式進入背景或結束時就不會讀取感測器了
						smgr.unregisterListener(mListener, lightSensor.get(0));
			 			isStarted = false;
						if(m_nTime >= check_Time+10){
			 				smgr.registerListener(mListener,slist.get(0), SensorManager.SENSOR_DELAY_UI);
				 			smgr.registerListener(mListener, lightSensor.get(0),SensorManager.SENSOR_DELAY_UI);
				 			m_nTime=0;
				 			max=(float)-10.0;
				 			check = false;
				 			isStarted = true;
			 			}*/

                    }

                }
		   		/*else if (check && m_nTime >= check_Time+30){
		   			if(gcom=="NO"){
			   			smgr.registerListener(mListener,slist.get(0), SensorManager.SENSOR_DELAY_UI);
			 			smgr.registerListener(mListener, lightSensor.get(0),SensorManager.SENSOR_DELAY_UI);
			 			isStarted = true;
		   			}
				   	mHandlerTime.removeCallbacks(timerRun);
				   	isStarted = false;
				   	smgr.unregisterListener(mListener, slist.get(0));//當程式進入背景或結束時就不會讀取感測器了
				   	smgr.unregisterListener(mListener, lightSensor.get(0));
				   	m_nTime=0;
				    //gcom=" ";
				   	check = false;
		   		}*/
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        //鎖定螢幕方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //textView1 =(TextView)findViewById(R.id.textView1);
        Button start = (Button)findViewById(R.id.button);
        start.setOnClickListener(start_l);
        //	Button stop = (Button)findViewById(R.id.stop);
        //	stop.setOnClickListener(stop_l);

        //三軸加速器
        smgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        slist = smgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
		/*if (slist.size() == 0) {
			Toast.makeText(this, "No accelerometer sensor",Toast.LENGTH_SHORT).show();
			finish();
		}*/

        //光感器
        lightSensor = smgr.getSensorList(Sensor.TYPE_LIGHT);

        isStarted = false;

        //計時器
        mHandlerTime.postDelayed(timerRun,1000);
        mHandlerTime.removeCallbacks(timerRun);
    }

    public void onDestroy(){
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }	@Override
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

    public String YZcompare(/*float cx,float cy,float cz*/){
        if (Math.abs(arrayY[m_nTime-1]) < 4 && Math.abs(arrayZ[m_nTime-1]) < 4)//2.75
            return "NO";
        else{
            if(Math.abs(arrayY[m_nTime-1]) >= Math.abs(arrayZ[m_nTime-1])){
                switch (XYcompare(arrayX[m_nTime-1], arrayY[m_nTime-1])){
                    case 'P':
                        return "BW";
                    case 'M':
                        return "NO";
                    case 'N':
                        return "FW";
                }
            }
            else{
                switch (XZcompare(arrayX[m_nTime-1], arrayZ[m_nTime-1])){
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
        if (xa>0){
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
