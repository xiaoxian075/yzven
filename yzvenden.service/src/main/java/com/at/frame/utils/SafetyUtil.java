package com.at.frame.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SafetyUtil {
    private static final byte[] COVER_BYTES = "4d5a5a4fda454t4y463423".getBytes();

    /**
     * 取得一个长度为8的倍数的byte字节数组
     * 不足位将按COVER_BYTES补位
     * @param key
     * @return
     */
    public static byte[] getKey(String key){
        int len = key == null ? 0 : key.length();
        byte[] bytes = key.getBytes();
        if ((len & 7) != 0) {
            int minLen = (len + 7) & ~7;
            bytes = Arrays.copyOf(bytes, minLen);
            System.arraycopy(COVER_BYTES, 0, bytes, len, minLen - len);
        }
        return bytes;
    }

    /**
     * DES解密
     * @param str   要解密的Base64字符串
     * @param key   密钥
     * @return      解密后的文本信息
     */
    public static String desDecrypt(String str,String key){
        byte[] vals = Base64.decode(str);
        byte[] bytes = getKey(key);
        SecureRandom random = new SecureRandom();
        try{
            DESKeySpec desKey = new DESKeySpec(bytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            return new String(cipher.doFinal(vals));
        }catch (Exception e){

        }
        return str;
    }

    /**
     * DES加密
     * @param str   要加密的字符串
     * @param key   密钥
     * @return      加密后的base64字符串
     */
    public static String desEncrypt(String str, String key) {
        byte[] bytes = getKey(key);
        SecureRandom random = new SecureRandom();
        try {
            DESKeySpec desKey = new DESKeySpec(bytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            return Base64.encode(cipher.doFinal(str.getBytes()));
        } catch (Exception e) {

        }
        return str;
    }

    /**
     * 取一个Md5加密
     * @param str   要加密的值
     * @return      加密后的md5
     */
    public static String getAbnormalMd5(String str) {
        if (str == null) str = "167";
        str = str.concat("__11__66Z77");
        str = getMd5(str, 2);
        return str.replace("b", "2");
    }

    /**
     * 取一个Md5加密
     * @param str   要加密的值
     * @return      加密后的md5
     */
    public static String getMd5(String str) {
        return getMd5(str, 1);
    }

    /**
     * 取一个Md5加密
     * @param str       要加密的值
     * @param times     循环加密次数
     * @return          加密后的md5
     */
    public static String getMd5(String str, int times) {
        StringCache buf = new StringCache(32);
        String encode = str;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (int j = 0; j < times; j++) {
                md.update(encode.getBytes());
                byte b[] = md.digest();
                int i;
                for (int offset = 0, l = b.length; offset < l; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                encode = buf.toString();
                buf.clear();
            }
            md = null;
            buf = null;
        } catch (NoSuchAlgorithmException e) {

        }
        return encode;
    }
}
