package com.company.project.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    private String key = "key";

    public String md5(String text) {
        return DigestUtils.md5Hex(text + this.key);
    }
}