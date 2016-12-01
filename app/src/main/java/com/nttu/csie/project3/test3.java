package com.nttu.csie.project3;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xm.library.view.XMFadeBarHelper;
import com.xm.library.view.XMSettings;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class test3 extends FragmentActivity
{

    /**
     * listview
     */
    //@InjectView(R.id.lv_my)
    ListView mlistview;
    /**
     * 刷新框架
     */
    //@InjectView(R.id.rl_refresh)
    BGARefreshLayout mRefreshLayout;

    /**
     * 上下文
     */
    private Context mContext;
    private List mlist;
    /**
     * actionbar对象
     */
    private ActionBar mActionBar;

    /**
     * 辅助类
     */
    private XMFadeBarHelper helper;


    /**
     * headerview
     */
    private View llheaderview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        //super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Fragment01", PageFragment.class)
                .add("Fragment02", PageFragment.class)
                .add("Fragment03", PageFragment.class)
                .add("Fragment04", PageFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

}
