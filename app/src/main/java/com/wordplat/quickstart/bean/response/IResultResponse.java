package com.wordplat.quickstart.bean.response;

/**
 * <p>IResultResponse</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public interface IResultResponse {

    /**
     * 服务器返回的逻辑结果响应码
     */
    int getResultCode();

    /**
     * 服务器返回的逻辑结果错误信息
     */
    String getResultDescr();
}
