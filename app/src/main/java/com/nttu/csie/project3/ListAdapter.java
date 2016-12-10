package com.nttu.csie.project3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class ListAdapter extends BaseAdapter {
    private LayoutInflater mLayInf;
    List<Map<String, Object>> mItemList;

    public ListAdapter(Context context, List<Map<String, Object>> itemList)
    {
        mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemList = itemList;
    }

    //取得 ListView 列表 Item 的數量。通常數量就是從建構子傳入的陣列或是集合大小。
    @Override
    public int getCount() {
        //取得 ListView 列表 Item 的數量
        return mItemList.size();
    }

    //取得 ListView 列表於 position 位置上的 Item。position 通常是資料在陣列或是集合上的位置。
    @Override
    public Object getItem(int position) {
        //取得 ListView 列表於 position 位置上的 Item
        return position;
    }

    //取得 ListView 列表於 position 位置上的 Item 的 ID，一般用 position 的值即可。
    @Override
    public long getItemId(int position) {
        //取得 ListView 列表於 position 位置上的 Item 的 ID
        return position;
    }

    //通常會設定與回傳 convertView 作為顯示在這個 position 位置的 Item 的 View。
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //設定與回傳 convertView 作為顯示在這個 position 位置的 Item 的 View。
        View v = mLayInf.inflate(R.layout.list_item, parent, false);

        ImageView imgView = (ImageView) v.findViewById(R.id.list_image);
        TextView txtView1 = (TextView) v.findViewById(R.id.list_item1);
        TextView txtView2 = (TextView) v.findViewById(R.id.list_item2);

        imgView.setImageResource(Integer.valueOf(mItemList.get(position).get("Item Icon").toString()));
        txtView1.setText(mItemList.get(position).get("Item Date").toString());
        txtView2.setText(mItemList.get(position).get("Item Location").toString());

        return v;
    }
}
