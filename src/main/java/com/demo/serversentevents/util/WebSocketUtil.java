package com.demo.serversentevents.util;

import java.net.URI;

public class WebSocketUtil {

    public static String getParam(URI uri, String key) {

        String query = uri.getQuery();
        if (query != null) {
            String[] arr = query.split("&");
            for (String param: arr) {
                if (key.equals(param.split("=")[0])) {
                    return param.split("=")[1];
                }
            }
        }
        return null;
    }

}
