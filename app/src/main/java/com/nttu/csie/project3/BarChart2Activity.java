package com.nttu.csie.project3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BarChart2Activity extends AppCompatActivity {

    //日期
    Button dateButton;
    Calendar calendar;
    int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    //柱狀圖
    BarChart chart;

    MyDB db = null;

    private int DATA_COUNT = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_bar_chart2);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        processViews();

        //日期處理
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        getDatetime();
        dateButton.setText(setDateFormat(mYear,mMonth,mDay));

        //設置圖表敘述
        chart.setDescription("");
        chart.setData(getBarData());
        configChartAxis(chart);

        dateButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(0);
                datePickerDialog.updateDate(mYear, mMonth, mDay);
            }

        });
    }


    protected void processViews(){
        chart = (BarChart)findViewById(R.id.barChart2);
        dateButton = (Button)findViewById(R.id.dateButton2);
        db = new MyDB(getApplicationContext());

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                mYear = year;
                mMonth = month;
                mDay = day;

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                getDatetime();
//                dateButton.setText(setDateFormat(year,month,day));
            }

        }, mYear,mMonth, mDay);
        return datePickerDialog;
    }


    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "年"
                + String.valueOf(monthOfYear + 1) + "月"
                + String.valueOf(dayOfMonth) + "日";
    }


    //X軸數據
    private List<String> getLabels(){
        List<String> xVals = new ArrayList<>();
        for(int i=0;i<DATA_COUNT;i++){
            xVals.add(i * 2+"hr");
        }
        return xVals;
    }


    //Y軸數據
    private List<BarEntry> getChartData(){

        int start = 1;
        Cursor c = null;
        long time=calendar.getTimeInMillis();

        List<BarEntry> yVals = new ArrayList<>();
        for(int i=0;i<DATA_COUNT;i++) {
            c = db.get(time + (i*2) * 3600 * 1000);
            yVals.add(new BarEntry(c.getLong(0)*40, i));

        }
        return yVals;
    }


    private BarData getBarData(){
        BarDataSet dataSetA = new BarDataSet(getChartData(), "cal");

        List<IBarDataSet> dataSets = new ArrayList<>();
        // add the datasets
        dataSets.add(dataSetA);
        //設定顏色
        dataSetA.setColors(getChartColors());

        return new BarData(getLabels(), dataSets);
    }


    private int[] getChartColors() {
        int[] colors = new int[]{
                getResourceColor(R.color.chart_color_KR) };
        return colors;
    }

    private int getResourceColor(int resID){
        return getResources().getColor(resID);
    }

    //config
    private void configChartAxis(BarChart chart_bar){
        XAxis xAxis = chart_bar.getXAxis();

        //消除隔線
        xAxis.setDrawGridLines(false);

        YAxis leftYAxis = chart_bar.getAxisLeft();
        leftYAxis.setDrawGridLines(false);

        YAxis RightYAxis = chart_bar.getAxisRight();
        RightYAxis.setEnabled(false);   //不顯示右側


        //改變X軸顯示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void getDatetime(){
        //日期處理
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        dateButton.setText(setDateFormat(mYear,mMonth,mDay));
        chart.setData(getBarData());
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
