package com.wordplat.quickstart.utils;

import android.content.Context;
import android.util.Log;

import com.wordplat.quickstart.R;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * <p>SSLHelper</p>
 * <p>Date: 2017/5/29</p>
 *
 * @author afon
 */

public class SSLHelper {
    private static final String KEY_STORE_TYPE_BKS = "bks"; // 证书类型 固定值
    private static final String KEY_STORE_TYPE_P12 = "PKCS12"; // 证书类型 固定值

    private static final int KEY_STORE_CLIENT_PATH = R.raw.client; // 客户端要给服务器端认证的证书
    private static final String KEY_STORE_PASSWORD = "123456"; // 证书密码

    private static final int KEY_STORE_TRUST_PATH = R.raw.mytruststore; // 客户端验证服务器端的证书库
    private static final String KEY_STORE_TRUST_PASSWORD = "123456"; // 证书库密码

    /**
     * 获取SSLContext
     *
     * @param context 上下文
     * @return SSLContext
     */
    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

            InputStream ksIn = context.getResources().openRawResource(KEY_STORE_CLIENT_PATH);
            InputStream tsIn = context.getResources().openRawResource(KEY_STORE_TRUST_PATH);
            try {
                keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            Log.e("SSLHelper", e.getMessage(), e);
        }
        return null;
    }
}
