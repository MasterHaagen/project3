package com.nttu.csie.project3;

import java.util.Date;
import java.util.Locale;

public class Item implements java.io.Serializable {

    // 編號
    private long id;
    //起始時間
    private long start_time;
    //運動種類
    private long activity_type;
    //結束時間
    private long end_time;
    //步數
    private long step_count;
    //跌倒偵測
    private boolean selected;

    public Item() {
//        title = "";
//        content = "";
//        color = Colors.LIGHTGREY;
        start_time = 0;
        activity_type = 0;
        end_time = 0;
        step_count = 0;
    }

    public Item(long id, long start_time, long activity_type, long end_time, long step_count) {
        this.id = id;
        this.start_time = start_time;
        this.activity_type = activity_type;
        this.end_time = end_time;
        this.step_count = step_count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStarttime() {
        return start_time;
    }

    public void setStarttime(long start_time) {
        this.start_time = start_time;
    }

    public long getActivitytype() {
        return activity_type;
    }

    public void setActivitytype(long activity_type) {
        this.activity_type = activity_type;
    }

    public long getEndtime() {
        return end_time;
    }

    public void setEndtime(long end_time) {
        this.end_time = end_time;
    }

    public long getSteps() {
        return step_count;
    }

    public void setSteps(long step_count) {
        this.step_count = step_count;
    }



//    public boolean isSelected() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public String getRecFileName() {
//        return recFileName;
//    }
//
//    public void setRecFileName(String recFileName) {
//        this.recFileName = recFileName;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
//
//    public long getLastModify() {
//        return lastModify;
//    }
//
//    public void setLastModify(long lastModify) {
//        this.lastModify = lastModify;
//    }
//
//    // 裝置區域的日期時間
//    public String getLocaleDatetime() {
//        return String.format(Locale.getDefault(), "%tF  %<tR", new Date(datetime));
//    }
//
//    // 裝置區域的日期
//    public String getLocaleDate() {
//        return String.format(Locale.getDefault(), "%tF", new Date(datetime));
//    }
//
//    // 裝置區域的時間
//    public String getLocaleTime() {
//        return String.format(Locale.getDefault(), "%tR", new Date(datetime));
//    }

}