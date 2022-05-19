package org.edrik.tool.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * @author mohuangNPC
 * @version 1.0
 * @date 2022/5/17 14:10
 */
public class Encrypt {

    /**
     * 对字符串进行sha1加密
     *
     * @param inStr 需要加密的字符串
     * @return      加密后的字符串
     * @throws Exception
     */
    public static String SHA1(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 生成16位大小写数字随机字符串
     * @return
     */
    public static String getRandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<16;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成指定长度随机字符串
     * @param length 长度
     * @return
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     *
     * 生成指定长度类型的随机字符串
     * @param length 长度
     * @param type  类型 1: 纯小写字母 2: 纯大写字母 3: 大小写子字母 4: 纯数字 5: 大小写字母加数字
     * @return
     */
    public static String getRandomString(int length, int type){
        String str = "";
        if(type == 1){
            str = "abcdefghijklmnopqrstuvwxyz";
        }else if(type == 2){
            str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }else if(type == 3){
            str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }else if(type == 4){
            str = "0123456789";
        }else {
            str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * MD5加密
     * @param str
     * @return
     */
    public static String MD5(String str){
        return DigestUtils.md2Hex(str);
    }

    /**
     * pem证书转PrivateKey 微信可用
     * @param path 证书路径
     * @return
     */
    public static PrivateKey PrivateKey(String path){
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)), "utf-8");
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("当前Java环境不支持RSA");
        } catch (InvalidKeySpecException e) {
            System.err.println("无效的密钥格式");
        } catch (Exception e){
            System.err.println("初始化证书失败");
        }
        return null;
    }
}
