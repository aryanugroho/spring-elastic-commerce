/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class HttpUtil {
    
    /**
     * Removes page,size kind of parameters from request url
     * @param request
     * @return 
     */
    public static String getPageUrl(HttpServletRequest request) {        
        String url = request.getRequestURI() + "?" + request.getQueryString();
        url = url.replaceAll("[&?]page.*?(?=&|\\?|$)", "")
                .replaceAll("[&?]size.*?(?=&|\\?|$)", "");
        return url ;
    }
}
