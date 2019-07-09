package com.example.speedcapitalltd.utilities;

import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LogInApiService {

    private static final int HTTPPORT=80;
    private  static final int HTTPSPORT=4200;
    private static final AsyncHttpClient CLIENT = new AsyncHttpClient(true,HTTPPORT,HTTPSPORT);


    /**
     * Post.
     *
     * @param url             the url
     * @param params          the params
     * @param responseHandler the response handler
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        CLIENT.post(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Method returns full url
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return ApiConstants.BASE_URL + relativeUrl;
    }
}
