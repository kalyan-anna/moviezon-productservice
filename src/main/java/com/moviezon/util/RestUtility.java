package com.moviezon.util;

import static org.apache.commons.lang3.StringUtils.*;

import javax.servlet.http.HttpServletRequest;

public class RestUtility {

    private static String BASE_URL;

    public static String getBaseUrl(HttpServletRequest request) {
        if(isEmpty(BASE_URL)) {
            StringBuilder baseURL = new StringBuilder();
            baseURL.append(request.getScheme());
            baseURL.append("://");
            baseURL.append(request.getServerName());
            if (request.getServerPort() != 80) {
                baseURL.append(":");
                baseURL.append(request.getServerPort());
            }
            if(isNotEmpty(request.getContextPath())) {
                baseURL.append(request.getContextPath());
            }
            BASE_URL = baseURL.toString();
        }
        return BASE_URL;
    }
}
