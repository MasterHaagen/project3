package com.nttu.csie.project3;

import java.util.Calendar;
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

public class mix extends Activity {
    MyDB db;
    //�^��-----------------------------------------------------------------------
    float Fmax;
    float Force;
    float totalForce = (float)0.0;
    float x,y,z;
    float gx,gy,gz;
    float ggx,ggy,ggz;
    float[] agy = new float[100];
    float[] agz = new float[100];
    float sma;
    boolean check = false;
    boolean check1 = false;
    boolean fall=false;
    int check_Time = -1;
    String gcom;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    boolean isStarted;
    boolean mJustCall;
    boolean checkCall;
    boolean move=false;
    double degree;
    int nowTime = 0;
    int sensorTime = 0;
    //step--------------------------------------------------------------
    float dx = 0.0f;
    float dy = 0.0f;
    boolean up=false;
    boolean down=false;
    float max = 0;
    float min = 0;
    float[] fx = {100};
    //float[] fy = {100};
    int totalStep = 0;
    int effectiveStep = 0;
    boolean isStart = false;
    int sensorCount = 0;
    float tempMax = -99;
    float tempMin = 99;
    boolean walking = false;
    boolean changed = false;
    float gateValue = 0;
    float[] gvAverage = new float[10];
    int gvCount = 0;
    float buttonTime = -1;
    float buttonTime2 = -1;
    float tempButtonTime = 0;
    float top = 0;
    float button = 0;
    private SensorManager smgr;
    private Sensor mAccelerometer;
    private List<Sensor> slist;
    private float[] arrayX= new float[100];
    private float[] arrayY= new float[100];
    private float[] arrayZ= new float[100];
    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event){
            // TODO Auto-generated method stub
            //if(check_Time != sensorCount){
            if (event.sensor == slist.get(0)) {
                if (isStarted == false) return;
                if (nowTime == sensorTime) return;
                sensorTime = nowTime;
                if(sensorCount < 99)
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
                degree=Math.sin(event.values[0]/SensorManager.GRAVITY_EARTH);

                if(sensorCount>=6){
                    totalForce += Math.pow((float)(arrayX[sensorCount]+arrayX[sensorCount-1]-arrayX[sensorCount-2]-arrayX[sensorCount-3])/4/SensorManager.GRAVITY_EARTH, 2.0);//X
                    totalForce += Math.pow((float)(arrayY[sensorCount]+arrayY[sensorCount-1]-arrayY[sensorCount-2]-arrayY[sensorCount-3])/4/SensorManager.GRAVITY_EARTH, 2.0);//Y
                    totalForce += Math.pow((float)(arrayZ[sensorCount]+arrayZ[sensorCount-1]-arrayZ[sensorCount-2]-arrayZ[sensorCount-3])/4/SensorManager.GRAVITY_EARTH, 2.0);//Z
                    totalForce = (float)Math.sqrt(totalForce);
                    if (totalForce > Fmax){
                        Fmax = totalForce;
                    }

                    if (totalForce > 2 ){

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
                        if(check_Time == -1)
                            check_Time = nowTime;

                    }

                }
                if(move){
                    if(check_Time != -1 && nowTime > check_Time+1){//0.2��X��
                        if(Math.abs(degree)<=0.6){
                            gcom = YZcompare();
                            if(gcom!="NO") fall=true;
                            textView2.setText("Fmax: " + Fmax+"\n"+"\n"+gcom+"\n"+"\n"+degree+"\n"+"\n");
                            /**
                             **/
                            Calendar c = Calendar.getInstance();
                            db.insert2(c.getTimeInMillis(),gcom,"");
                             /**
                             **/
                            move=false;
                            Fmax=0;
                        }
                        else{
                            move=false;
                        }
                        check_Time=-1;
                    }
                }
                if(fall) return;
                //step------------------------------------------------------------------------------------------------------------------
                if(sensorCount < 5) return;//�ݭn4���H�W��Ƥ~�i���B�z
                fx[sensorCount] = filter(arrayX[sensorCount], arrayX[sensorCount-1], arrayX[sensorCount-2], arrayX[sensorCount-3]);
                if(sensorCount < 6) return; //2���H�W�B�z�L����Ƥ~�i�����
                dx = fx[sensorCount]-fx[sensorCount-1];
                up_and_down(dx);
                if(nowTime % 10 ==0){//�C���s
                    max = tempMax;
                    min = tempMin;
                    tempMax = -99;
                    tempMin = 99;
                    changed = true;
                }
                if(max - min > 1){//���T���j�~�⨫��
                    if(changed){//�C����@���֭�
                        int icount = 0;
                        gateValue = (max+min)/2;
                        //�����֭�-------------------------
							/*gvAverage[gvCount] = gateValue;
							gateValue = 0;
							for(int i = 0; i<10; i++){
								if (gvAverage[i] != 0)
									gateValue += gvAverage[i];
								else {
									icount = (i+1);
									break;
								}
							}
							gateValue  = gateValue / icount;
							if(gvCount < 9)
								gvCount++;
							else
								gvCount = 0;*/
                        //----------------------------------
                        //timeCount = timeNow;
                        changed = false;
                    }
                    if(top > gateValue-0.5){
                        if(button < (gateValue-0.5) && button != -1){
                            if(buttonTime == -1)
                                buttonTime = tempButtonTime;
                            else
                                buttonTime2 = tempButtonTime;
                            button = -1;
                        }
                    }
                    if (buttonTime2 != -1){

                        if (buttonTime2 - buttonTime >= 2 && buttonTime2 - buttonTime <= 20){
                            effectiveStep++;
                            textView3.setText("step= "+effectiveStep);
                            buttonTime = buttonTime2;
                            //�s�򦳮ĨB��----------------
								/*if (effectiveStep > 5){
									if (walking){
										totalStep++;
									} else {
										walking = true;
										totalStep += effectiveStep;
									}
								}*/
                            //------------------------
                        } else if (buttonTime2 - buttonTime < 2) {
                            //walking = false;
                            //effectiveStep = 0;
                            gvAverage = new float[10];
                            gvCount = 0;

                        } else if (buttonTime2 - buttonTime > 20){
                            gvAverage = new float[10];
                            gvCount = 0;
                            buttonTime = -1;
                        }else{
                            gvAverage = new float[10];
                            gvCount = 0;
                            buttonTime = -1;
                        }
                        buttonTime2 = -1;
                    }


                } /*else {
						effectiveStep = 0;
						buttonTime = 0;
						buttonTime2 = 0;
						walking = false;
						gvAverage = new float[10];
						gvCount = 0;
						X="";
						GV="";
					}*/
                //textViewX.setText("Step: "+totalStep);

            }
        }

        //��T�ק��ܷ|�I�s
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
    private List<Sensor> lightSensor ;
    private Handler mHandlerTime = new Handler();
    private final Runnable timerRun = new Runnable(){
        public void run(){
            mHandlerTime.postDelayed(this, 200);

            nowTime++; // �g�L����� + 1
            if(nowTime >= 6000){
                nowTime=0;
                sensorTime = 0;
                check_Time = -1;
                buttonTime = -1;
                buttonTime2 = -1;
            }
        }
    };
    OnClickListener start_1 = new OnClickListener(){
        @Override
        public void onClick(View v){
            if(isStarted){
                smgr.unregisterListener(mListener);
                mHandlerTime.removeCallbacks(timerRun);
                isStarted = false;
            } else {
                textView1.setText(" ");
                isStarted = true;
                check_Time = -1;
                Fmax = (float)-10.0;
                Force=(float)0.0;
                arrayX= new float[100];
                arrayY= new float[100];
                arrayZ= new float[100];
                nowTime=0;
                sensorTime = 0;
                //step--------------------------
                dx = 0.0f;
                dy = 0.0f;
                up=false;
                down=false;
                max = 0;
                min = 0;
                fx = new float[100];
                //fy = new float[100];
                totalStep = 0;
                effectiveStep = 0;
                sensorCount = 0;
                tempMax = -99;
                tempMin = 99;
                walking = false;
                gateValue = 0;
                gvAverage = new float[10];
                gvCount = 0;
                buttonTime = -1;
                buttonTime2 = -1;
                smgr.registerListener(mListener,mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                timerRun.run();
                smgr.registerListener(mListener, lightSensor.get(0),SensorManager.SENSOR_DELAY_UI);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_mix);

        textView1 =(TextView)findViewById(R.id.tv1);
        textView2 =(TextView)findViewById(R.id.tv2);
        textView3 =(TextView)findViewById(R.id.tv3);
        textView4 =(TextView)findViewById(R.id.tv4);
        Button start = (Button)findViewById(R.id.bt1);
        start.setOnClickListener(start_1);

        //�T�b�[�t��
        smgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        slist = smgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        mAccelerometer  = smgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //���P��
        lightSensor = smgr.getSensorList(Sensor.TYPE_LIGHT);

        isStarted = false;

        db = new MyDB(getApplicationContext());
        //�p�ɾ�
        //  mHandlerTime.postDelayed(timerRun,1000);
        //  mHandlerTime.removeCallbacks(timerRun);
    }

    @Override
    public void onDestroy(){
        smgr.unregisterListener(mListener);
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    void up_and_down(float delta){
        if(delta>=0){
            up=true;
            getpoint(true);
        }
        else{
            down=true;
            getpoint(false);
        }
    }

    void getpoint(boolean status){
        if(status){
            if(down){
                down=false;
                button = fx[sensorCount-1];
                tempButtonTime = nowTime;
                if(fx[sensorCount-1] < tempMin)
                    tempMin = fx[sensorCount-1];
            }
        }
        else {
            if(up){
                up=false;
                top = fx[sensorCount-1];
                if(fx[sensorCount-1] > tempMax)
                    tempMax = fx[sensorCount-1];
            }
        }
    }

    float filter(float now, float last1, float last2, float last3){
        return (now+last1+last2+last3)/4;
    }

    public String YZcompare(/*float cx,float cy,float cz*/){
        if (Math.abs(arrayY[sensorCount]) < 4 && Math.abs(arrayZ[sensorCount]) < 4)//2.75
            return "NO";
        else{
            if(Math.abs(arrayY[sensorCount]) >= Math.abs(arrayZ[sensorCount])){
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
                switch (XZcompare(arrayX[sensorCount], arrayZ[sensorCount])){
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
