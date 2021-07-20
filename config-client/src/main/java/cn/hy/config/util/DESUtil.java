package cn.hy.config.util;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密解密工具类
 * @author wlm
 *
 */
public class DESUtil {

	 /**
     * 偏移变量，固定占8位字节
     */
    private static final String IV_PARAMETER = "20191203";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";
 
	/**
	 * DES加密 转 Base64字符串
	 * 
	 * @param srcStr 待加密原文
	 * @param sKey 密码
	 * @return base64Str 密文
	 */
	public static String encryptTobase64(String srcStr, String sKey) throws Exception {
		byte[] src = srcStr.getBytes(CHARSET);
		byte[] buf = des(Cipher.ENCRYPT_MODE, sKey, src);
		return Base64.getEncoder().encodeToString(buf);
	}

	/**
	 * DES解密 根据 Base64字符串
	 *
	 * @param base64Str 加密密文
	 * @param sKey 密码
	 * @return srcStr 异常时返回源字符串 base64Str
	 */
	public static String decryptBybase64(String base64Str, String sKey) {
		try {
			byte[] src = Base64.getDecoder().decode(base64Str);
			byte[] buf;
			buf = des(Cipher.DECRYPT_MODE, sKey, src);
			return new String(buf, CHARSET);
		} catch (Exception e) {
			System.out.println("password decrypt error :"+e.getMessage()+"return src string");
		}
		return base64Str;
	}
	
	
	 
    /**
     * DES 加解密
     * @param mode Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE  
     * @param key 
     * @param data String 字符串数据
     * @return 二进制密文
     * @throws Exception
     */
	private static byte[] des(int mode, String key, byte[] data) throws Exception {
        Key secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
        cipher.init(mode, secretKey, iv);
        return cipher.doFinal(data);
    }

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }
    
	
	private DESUtil() {}
	
}
