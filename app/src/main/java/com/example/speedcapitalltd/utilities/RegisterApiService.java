package com.example.speedcapitalltd.utilities;

import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterApiService {

    private  static  final int HTTPPORT = 80;
    private static  final int HTTPSPORT= 42000;
    private static final AsyncHttpClient CLIENT = new AsyncHttpClient(true,HTTPPORT,HTTPSPORT);
/** * Register.
     *
             * @param url             the url
     * @param params          the params
     * @param responseHandler the response handler
     */

public  static  void register (String url, RequestParams params, AsyncHttpResponseHandler responseHandler){

    CLIENT.post(ApiConstants.BASE_URL+url,params,responseHandler);
}
}
