package com.nttu.csie.project3;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StepTest extends Activity implements SensorEventListener {
    float[] x = new float[100];
    float[] y = new float[100];
    float[] z = new float[100];
    float dx = 0.0f;
    float dy = 0.0f;
    boolean dw = false;
    boolean fw = false;
    boolean going = false;
    String strx = "";
    String stry = "";
    String strz = "";
    boolean countswitch = true;
    int step = 0;
    boolean is_start = false;
    int time_count;
    MyDB db;
    private TextView textViewX;
    private Button start;
    private SensorManager mSensorManager;
    private List<Sensor> slist;
    private Sensor mAccelerometer;
    private Handler mHandlerTime = new Handler();
    private final Runnable timerRun = new Runnable(){
        public void run(){
            mHandlerTime.postDelayed(this, 100);
        }
    };
    OnClickListener startfirst = new OnClickListener(){
        @Override
        public void onClick(View v){
            if(is_start) restart(); else onResume();
            textViewX.setText("Start");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        textViewX =(TextView)findViewById(R.id.textViewX);
        start = (Button)findViewById(R.id.btnStart);
        start.setOnClickListener(startfirst);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer  = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		/*mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.unregisterListener(this);
		mHandlerTime.postDelayed(timerRun,1000);
        mHandlerTime.removeCallbacks(timerRun);*/
        db = new MyDB(getApplicationContext());
    }

    public void onDestroy(){
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(countswitch){
            x[0] = Math.abs(event.values[0]);
            y[0] = Math.abs(event.values[1]);
            //z[count] = event.values[2];
            dx = x[0]-x[1];
            dy = y[0]-y[1];
            countswitch = false;
        }else{
            x[1] = Math.abs(event.values[0]);
            y[1] = Math.abs(event.values[1]);
            //z[count] = event.values[2];
            dx = x[1]-x[0];
            dy = y[1]-y[0];
            countswitch = true;
        }


        if(dx < -0.5)
            dw = true;
        else if (dx > 0.5)
            dw = false;

        if(dy > 0.8)
            fw = true;
        else if (dy < -0.8)
            fw = false;

        if(dw && fw) going = true;

        if(going && !dw && !fw){
            step+=2;
            add(step);
            going = false;
        }


        textViewX.setText("Step: "+step);
		/*count++;
		if(count >=20) {
			for(int i=0;i<=count;i++){
				strx += " + "+x[i];
				stry += " + "+y[i];
				strz += " + "+z[i];
			}
			textViewX.setText(	"X: "+strx+"\n"+
								"Y: "+stry+"\n"+
								"Z: "+strz+"\n");
			onPause();
		}*/
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        x = new float[100];
        y = new float[100];
        z = new float[100];
        dx = 0.0f;
        dy = 0.0f;
        dw = false;
        fw = false;
        going = false;
        countswitch = true;
        step = 0;
        strx = "";
        stry = "";
        strz = "";
        is_start = true;
        timerRun.run();
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        onDestroy();
        is_start = false;
    }
    protected void restart(){
        onPause();
        onResume();

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
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
    public void add(int step){
        //Item item = new Item(0, new Date().getTime(), step);
        //db.insert(item);
    }
}
