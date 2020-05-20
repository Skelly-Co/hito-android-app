package com.skellyco.hito.core.util.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Encryptor {

    public static final String CHARACTER_ENCODING = "UTF-8";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    public static EncryptionResult encryptData(String alias, String dataToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));
        byte[] iv = cipher.getIV();
        byte[] encryption = cipher.doFinal(dataToEncrypt.getBytes(CHARACTER_ENCODING));
        return new EncryptionResult(encryption, iv);
    }

    private static SecretKey getSecretKey(String alias) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
        keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());
        return keyGenerator.generateKey();
    }
}
