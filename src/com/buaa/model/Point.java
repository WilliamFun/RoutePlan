package com.buaa.model;

/**
 * @author Zheng.Fan
 * @date 2022/09/15
 **/
public class Point {
    /**
     * 经度
     */
    double lng;
    /**
     * 纬度
     */
    double lat;
    /**
     * 海拔高度
     */
    double alt = 0;
    /**
     * 离地高度
     */
    double ht = 0;


    public Point(){
        lng = 0;
        lat = 0;
    }

    public Point(double lng, double lat){
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getHt() {
        return ht;
    }

    public void setHt(double ht) {
        this.ht = ht;
    }
}
