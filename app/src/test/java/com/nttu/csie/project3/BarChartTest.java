package com.nttu.csie.project3;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarChartTest extends AppCompatActivity {

    BarChart chart;
    private int DATA_COUNT = 5;
    private Random random;//用于产生随机数字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        chart = (BarChart)findViewById(R.id.barChart);
        chart.setData(getBarData());

    }


    private List<BarEntry> getChartData(){

        float start = 0f;

        List<BarEntry> chartData = new ArrayList<>();
        for(int i=(int) start;i<DATA_COUNT;i++){
            chartData.add(new BarEntry(i + 1f, i));
        }
        return chartData;
    }


    private BarData getBarData(){
        BarDataSet dataSetA = new BarDataSet(getChartData(), "LabelA");

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetA); // add the datasets

        return new BarData(getLabels(), dataSets);
    }

    private List<String> getLabels(){
        List<String> chartLabels = new ArrayList<>();
        for(int i=0;i<DATA_COUNT;i++){
            chartLabels.add("X"+i);
        }
        return chartLabels;
    }

}
