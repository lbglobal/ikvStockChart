package com.wordplat.quickstart.bean.response;

import org.xutils.http.annotation.HttpResponse;

/**
 * <p>ServerResponse</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

@HttpResponse(parser = ServerResponseParser.class)
public class ServerResponse implements IResultResponse {

    @Override
    public int getResultCode() {
        return 0;
    }

    @Override
    public String getResultDescr() {
        return null;
    }
}
