package com.example.speedcapitalltd.Configurations;

import com.example.speedcapitalltd.BuildConfig;

public class ApiConstants {

    public static String toolbar_header="";
    /**
     * The constant BASE_URL.
     * Hidden in the application Config Files
     */
    public static final String BASE_URL = BuildConfig.BASE_URL;
    /**
     * Default charset for JSON request.
     */
    public static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Used to validate emails
     */
    public static final String REGEXP =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

}
