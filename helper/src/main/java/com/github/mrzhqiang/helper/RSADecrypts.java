package com.github.mrzhqiang.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 解密工具。
 */
public final class RSADecrypts {
    private RSADecrypts() {
        // no instances.
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RSADecrypts.class);

    public static final int KEY_SIZE = 2048;

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(KEY_SIZE);
        return generator.generateKeyPair();
    }

    public static String publicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] encoded = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }

    public static String privateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] encoded = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }

    public static PublicKey parsePublicKey(String publicText) throws Exception {
        byte[] decode = Base64.getDecoder().decode(publicText);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(keySpec);
    }

    public static PrivateKey parsePrivateKey(String privateText) throws Exception {
        byte[] decode = Base64.getDecoder().decode(privateText);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(keySpec);
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) {
        try {
            byte[] bytes = Base64.getDecoder().decode(cipherText);
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(decryptCipher.doFinal(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("RSA 解密失败！将直接返回文本：" + cipherText, e);
            return cipherText;
        }
    }

}
