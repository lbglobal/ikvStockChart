package com.wordplat.quickstart.mvp.exception;

/**
 * <p>服务器返回错误码</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class ResultFailedException extends Exception {

    private int errCode;

    private String errMessage;

    public ResultFailedException(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
