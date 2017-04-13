package com.wordplat.quickstart.bean;

/**
 * <p>YahooKLineBean</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class YahooKLineBean {

    private float open;

    private float high;

    private float low;

    private float close;

    private int volume;

    private String date;

    public YahooKLineBean(float open, float high, float low, float close, int volume, String date) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getClose() {
        return close;
    }

    public int getVolume() {
        return volume;
    }

    public String getDate() {
        return date;
    }
}
