package com.wordplat.quickstart.bean;

/**
 * <p>BtcBean</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

public class BtcBean {

    private long date;

    private float price;

    private float amount;

    private long tid;

    private String type;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
