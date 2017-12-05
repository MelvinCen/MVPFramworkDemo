package com.melvin.mvpframworkdemo.network;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/2 15:10
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/12/2 15:10
 * @Description ${TODO}
 */

public class HttpsFactory {
    /**
     * ssl摘自网络，关闭了所有的SSL认证检查
     * @return
     */
    public static SSLSocketFactory getSslSocketFactory(){
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null,new TrustManager[]{xtm},new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();

    }

    public static HostnameVerifier getHostnameVerifier(){
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        return DO_NOT_VERIFY;
    }
}
