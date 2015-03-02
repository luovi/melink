package cn.meyoung.wlfz.webapi.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
/**
 * Created by goushicai on 15-2-26.
 */
public class AesUtils {
    //static final String KEY_ALGORITHM = "AES/CBC/PKCS5Padding";
    static final String KEY_ALGORITHM = "AES/CBC/NoPadding";
    static Charset CHARSET = Charset.forName("utf-8");
    /**
     * AES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密密钥，长度为32位字符
     * @param initVector    使用CBC模式，需要一个向量,增强算法的强度
     * @return 加密后并经Base64编码的字符串
     * @throws Exception 异常
     */
    public static String encode(String data,String key, String initVector) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET));

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] encrypt = cipher.doFinal(data.getBytes(CHARSET)); //加密
            String str = new String(encrypt,CHARSET);
            String result = Base64.encodeBase64String(encrypt);//Base64 编码

            return result;

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * AES算法，解密
     *
     * @param data 加密的字符串,该字符串经Base64加密
     * @param key  加密密钥，长度为32位字符
     * @param initVector    使用CBC模式，需要一个向量,增强算法的强度
     * @return 解密后的字符串
     * @throws Exception 异常
     */
    public static String decode(String data,String key, String initVector) throws Exception{
        try{

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET));

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器

            /* jdk 1.7 密钥长度超过16位报异常
            * 异常:java.security.InvalidKeyException: Illegal key size
            *
            * 下载 http://download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip
            * 解压,替换 $JAVA_HOME/jre/lib/security 目录下的local_policy.jar 与US_export_policy.jar
            * 在mac OS中 $JAVA_HOME 是 /Library/Java/JavaVirtualMachines/jdk1.7.0_71.jdk/Contents/Home/
            *
            **/

            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            // Base64解密
            byte[] content = Base64.decodeBase64(data.getBytes(CHARSET));
            String str = new String(content,CHARSET);
            //解密
            byte[] decrypt = cipher.doFinal(content);

            String result = new String(decrypt,CHARSET);

            return result;
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    //此方法暂时没用到
    public static SecretKey generateKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        /*
        * javax.crypto.BadPaddingException: Given final block not properly padded
        *
        * SecureRandom 实现完全随操作系统本身的內部状态，除非调用方在调用 getInstance 方法之后又调用了 setSeed 方法；
        * 该实现在 windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同。
        *
        */

         //防止linux下 随机生成key ,否则在解密时会报异常 javax.crypto.BadPaddingException: Given final block not properly padded

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
        secureRandom.setSeed(key.getBytes(CHARSET));

        keyGenerator.init(128,secureRandom);

        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;

    }

}
