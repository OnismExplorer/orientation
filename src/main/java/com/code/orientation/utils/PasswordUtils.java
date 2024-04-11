package com.code.orientation.utils;

import cn.hutool.crypto.digest.MD5;

/**
 * 密码工具类
 */
public class PasswordUtils {
    /**
     * 加密盐
     */
    static final byte[] SLAT = {'o', 'n', 'i', 's', 'm'};

    private PasswordUtils() {
    }

    /**
     * 加密
     */
    public static String encrypt(String password) {
        MD5 md5 = new MD5(SLAT);
        return md5.digestHex16(md5.digestHex(password));
    }

    /**
     * 密码配对
     */
    public static boolean match(String password, String encryptedPassword) {
        return !encryptedPassword.equals(encrypt(password));
    }
}
