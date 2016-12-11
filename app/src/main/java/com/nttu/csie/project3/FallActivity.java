package com.nttu.csie.project3;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttu.csie.project3.R.id.textView;

public class FallActivity extends AppCompatActivity {
    private static final String ITEM_DATE = "Item Date";
    private static final String ITEM_LOCATION = "Item Location";
    private static final String ITEM_ICON = "Item Icon";
    private ListView list_main;
    private ListAdapter mListAdapter;
    private MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("跌倒記錄");

        //mTxtR = (TextView) findViewById(R.id.testview);
        db = new MyDB(getApplicationContext());

        //宣告 ListView 元件
        list_main = (ListView)findViewById(R.id.list_main);
        list_main.setOnItemClickListener(listViewOnItemClickListener);

        //定義 ListView 每個 Item 的資料
        List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
        //String[] regionList = {"1", "2", "3", "4","5","6","7","8","9"};
        TypedArray regionIconList = getResources().obtainTypedArray(R.array.region_icon_list);

//        for (int i = 0; i < regionList.length; i++)
//        {
//            Map<String, Object> item = new HashMap<String, Object>();
//            item.put(ITEM_DATE, regionList[i]);
//            item.put(ITEM_LOCATION, "item:" + regionList[i]);
//            //item.put(ITEM_ICON, regionIconList.getResourceId(i, 0));
//            item.put(ITEM_ICON, regionIconList.getResourceId(0, 0));
//            itemList.add(item);
//        }

        Cursor c = db.getFall();
        int rows_num = c.getCount();
        if(rows_num != 0) {
            c.moveToFirst();			//將指標移至第一筆資料
            for(int i=0; i<rows_num; i++) {
//                int id = c.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
//                String name = c.getString(1);
//                int value = c.getInt(2);
                Calendar cal = Calendar.getInstance();
                Date dt = new Date(c.getLong(1));
                cal.setTime(dt);
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
                String str = df.format(cal.getTime());

                Map<String, Object> item = new HashMap<String, Object>();
                item.put(ITEM_DATE, str);
                item.put(ITEM_LOCATION, c.getString(3));
                //item.put(ITEM_ICON, regionIconList.getResourceId(i, 0));
                item.put(ITEM_ICON, regionIconList.getResourceId(0, 0));
                itemList.add(item);

                c.moveToNext();		//將指標移至下一筆資料
            }
        }

        // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
        mListAdapter = new ListAdapter(FallActivity.this, itemList);

        //設定 ListView 的 Adapter
        list_main.setAdapter(mListAdapter);

        c.close();
    }

    //設定 Item 的 OnClick 事件
    private AdapterView.OnItemClickListener listViewOnItemClickListener
            = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //TextView txtItemTitle = (TextView) view.findViewById(R.id.list_item1);
            Toast.makeText(getApplicationContext(), "你選擇的是"+position, Toast.LENGTH_SHORT).show();
            //TextView txtItemTitle = (TextView) view.findViewById(R.id.list_item2);
            //mTxtR.setText(txtItemTitle.getText());
            //String mTxtR = txtItemTitle.getText().toString();
            //Log.d("ListAdapter", mTxtR);
        }
    };
}
