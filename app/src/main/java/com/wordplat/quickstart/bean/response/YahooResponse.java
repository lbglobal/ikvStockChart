package com.wordplat.quickstart.bean.response;

import com.wordplat.quickstart.bean.YahooKLineBean;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * <p>YahooResponse</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

@HttpResponse(parser = YahooResponseParser.class)
public class YahooResponse extends ServerResponse {

    private List<YahooKLineBean> KLineList;

    public List<YahooKLineBean> getKLineList() {
        return KLineList;
    }

    public void setKLineList(List<YahooKLineBean> KLineList) {
        this.KLineList = KLineList;
    }
}
