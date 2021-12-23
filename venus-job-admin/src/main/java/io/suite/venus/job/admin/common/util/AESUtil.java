package io.suite.venus.job.admin.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

    //必须16位  否则会报错  偏移量
    private static String IV ="asdfgh1234567890";
    //钥匙  必须16位
   public static void main(String[] args) {
        String KEY = "passwordSalt-Job";
        //字符串编码
       System.out.println("编码结果:" + encrypt("admin",KEY));
       //字符串解码
//        System.out.println("编码结果:" + decrypt("AvLrRsmCqNwZ0xtl6rljFLmwqxH98Pn5F8fK7htQoNkHm7j3yM2ElAF4Mn4/ywteoDWPh35rlwods66l9JofJVo9EvrgdcVLM7ze5x41cIj3AZD4tiGlDiQdnVu7NyTsb10/thtNPwBM8p1uVP6qeQ/XH0/5v9+rH/e2jolYn+RlRcNvnUpcQ2i79KLvMA+zJz2ZCdWaMkv3CIuxRndpl+l8DFncWT2lJ3d82PTVDjPW3ICYDHB7R/YnUtWJGUFe",KEY));
    }



    //加密
    public static String encrypt(String str,String KEY) {
        try {
            byte[] bytes = str.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            bytes = cipher.doFinal(bytes);
            bytes = Base64.getEncoder().encode(bytes);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //解密
    public static String decrypt(String str,String KEY) {
        try {
            byte[] bytes = Base64.getDecoder().decode(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            bytes = cipher.doFinal(bytes);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
