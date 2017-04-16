package com.wordplat.quickstart.bean.response;

import com.wordplat.quickstart.bean.BtcBean;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * <p>BtcChinaResponse</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

@HttpResponse(parser = BtcChinaResponseParser.class)
public class BtcChinaResponse extends ServerResponse {

    private List<BtcBean> btcList;

    public List<BtcBean> getBtcList() {
        return btcList;
    }

    public void setBtcList(List<BtcBean> btcList) {
        this.btcList = btcList;
    }
}
