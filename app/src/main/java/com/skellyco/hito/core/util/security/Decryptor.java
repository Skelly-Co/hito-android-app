package com.skellyco.hito.core.util.security;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class Decryptor {

    public static final String CHARACTER_ENCODING = "UTF-8";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    public static String decryptData(String alias, byte[] encryptedData, byte[] encryptionIv) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException, NoSuchPaddingException, UnrecoverableEntryException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
        SecretKey secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        String decryptedString = new String(cipher.doFinal(encryptedData), CHARACTER_ENCODING);
        return decryptedString;
    }
}
