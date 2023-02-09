package com.kvitka.gateway.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilMethods {
    public static <T> String insertPathVariable(String url, String varName, T var) {
        String newURL = url.replace(String.format("{%s}", varName), var.toString());
        log.info("Path variable inserted (\"{}\" -> \"{}\")", url, newURL);
        return newURL;
    }
}
