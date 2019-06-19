package com.company.project.util;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
    public static SimpleDateFormat dateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        return sdf;
    }
}