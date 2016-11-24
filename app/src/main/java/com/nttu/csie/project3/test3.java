package com.nttu.csie.project3;




import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


public class test3 extends FragmentActivity

{

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

    public void onPageSelected(int position) {

        //.instantiateItem() from until .destoryItem() is called it will be able to get the Fragment of page.
        //Fragment page = adapter.getPage(position);

    }
}
