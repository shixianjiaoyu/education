package cn.sjjy.edu.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public final class Md5Util {
    private static final String DEFAULT_ENCODE = "UTF-8";
    private static final char HEX_CHARS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
    private static MessageDigest MD5_MESSAGE_DIGEST;
    static {
        MD5_MESSAGE_DIGEST=newMd5MessageDigest();
    }
    private static MessageDigest newMd5MessageDigest(){
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    private static MessageDigest newSHA1MessageDigest(){
        try {
            return MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private final static String md5Hash(String value, String key, String encode) {
        String digestStr = null;
        try {
            String input = value + key;
            byte[] bytes = input.getBytes(encode);
            byte[] rs = MD5_MESSAGE_DIGEST.digest(bytes);
            char[] chars = new char[rs.length << 1];
            int index = 0;
            for (int i = 0; i < rs.length; i++) {
                byte b = rs[i];
                chars[index++] = HEX_CHARS[b >>> 4 & 0xf];
                chars[index++] = HEX_CHARS[b & 0xf];
            }
            digestStr = new String(chars);
        } catch (Exception ex) {
        }
        return digestStr;
    }

    public final static String md5Hash(String value) {
        return md5Hash(value, "", DEFAULT_ENCODE);
    }

    public static void main(String[] args) {
        String value = "1qaz@WSX";
        System.out.println(md5Hash(value));
    }
}
