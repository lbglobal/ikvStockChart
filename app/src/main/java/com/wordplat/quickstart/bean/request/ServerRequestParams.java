package com.wordplat.quickstart.bean.request;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.wordplat.quickstart.BuildConfig;

import org.xutils.http.RequestParams;

import java.io.File;

/**
 * <p>ServerRequestParams</p>
 * <p>Date: 2017/4/5</p>
 *
 * @author afon
 */

public class ServerRequestParams extends RequestParams {
    private static final String TAG = "ServerRequestParams";

    private JSONObject params = null;

    private JSONObject requestJson= null;

    private String uploadKey = null;
    private Object uploadValue = null;

    public ServerRequestParams(String URL) {
        super(URL);
        params = new JSONObject();

        requestJson = new JSONObject();
    }

    public void addRequestParams(String key, Object value) {
        if (TextUtils.isEmpty(key) || value == null) {
            return;
        }

        requestJson.put(key, value);
    }

    public void addCustomParams(String key, JSONObject jsonObject) {
        if (TextUtils.isEmpty(key) || jsonObject == null) {
            return;
        }

        params.put(key, jsonObject);
    }

    /**
     * 上传文件
     *
     * @param value   可以是File, InputStream 或 byte[]
     */
    public void upload(Object value) {
        uploadKey = "uploadFile";
        uploadValue = value;
    }

    /**
     * 添加完参数或设置参数后调用此方法
     */
    public void commit() {
        params.put("RequestParams", requestJson);

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "##d 请求数据: " + requestJson.toJSONString());
        }

        addBodyParameter("", requestJson.toJSONString());

        if (!TextUtils.isEmpty(uploadKey) && uploadValue != null) {
            if (uploadValue instanceof org.json.JSONArray) {
                addParameter(uploadKey,  uploadValue);

            } else if(uploadValue instanceof String) {
                addBodyParameter(uploadKey, new File((String) uploadValue));

            } else if(uploadValue instanceof File) {
                addBodyParameter(uploadKey, (File)uploadValue);

            } else {
                addBodyParameter(uploadKey, uploadValue, "image/jpeg", System.currentTimeMillis()+".jpg");
            }
        }
    }

    @Override
    public String toString() {
        return params.toJSONString();
    }
}
