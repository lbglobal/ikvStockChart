package com.wordplat.quickstart.mvp;

/**
 * <p>
 * requestCode 请求编码 的意思是：
 *   对每一次 presenter 的调用指定一个唯一编码。
 *   比如，一个 Activity 中使用了两个以上的 presenter 方法，在 Activity 中 implement 了本接口，
 *   那么此时如果返回结果，就需根据请求编码来作对应的UI呈现。
 * </p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public interface BaseView {

    /**
     * 没有网络
     *
     * @param requestCode 请求编码
     */
    void onNoNetworkError(int requestCode);

    /**
     * 网络超时
     *
     * @param requestCode 请求编码
     */
    void onNetworkTimeOutError(int requestCode);

    /**
     * 操作成功
     *
     * @param requestCode 请求编码
     */
    void onSuccess(int requestCode);

    /**
     * 网络请求结果解析错误
     *
     * @param requestCode 请求编码
     */
    void onResultParseError(int requestCode);

    /**
     * 服务器返回失败、返回错误
     *
     * @param requestCode 请求编码
     * @param errCode 服务器返回的错误编码
     * @param errMessage 服务器返回的错误信息
     */
    void onResultFailed(int requestCode, int errCode, String errMessage);

    /**
     * 服务器返回结果为空，或者是解析数据出错，也视为空
     *
     * @param requestCode 请求编码
     */
    void onResultEmpty(int requestCode);

    /**
     * 显示一个警告信息
     *
     * @param requestCode 请求编码
     * @param errMessageResId 要显示的错误 String 的资源ID
     */
    void onShowWarning(int requestCode, int errMessageResId);
}
