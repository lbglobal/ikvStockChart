package com.wordplat.quickstart.bean;

/**
 * <p>KLineBean</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class KLineBean {

    private float open;

    private float high;

    private float low;

    private float close;

    private int volume;

    private String date;

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
