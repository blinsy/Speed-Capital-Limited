package com.example.speedcapitalltd.utilities;

import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * The type Get bank list api service.
 */
public class GetBankListApiService {

    /**
     * The Client.
     */
    private static final int HTTPPORT=80;
    private  static final int HTTPSPORT=4200;
    private static final AsyncHttpClient CLIENT = new AsyncHttpClient(true,HTTPPORT,HTTPSPORT);

    /**
     * Get bank list.
     *
     * @param accessToken     the access token
     * @param url             the url
     * @param requestParams   the request params
     * @param responseHandler the response handler
     */
    public static  void getBankList(String accessToken, String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler){
        CLIENT.addHeader("Authorization","Bearer "+accessToken);
        CLIENT.get(ApiConstants.BASE_URL+url,requestParams,responseHandler);
    }
}
