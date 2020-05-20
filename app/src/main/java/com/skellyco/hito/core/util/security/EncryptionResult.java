package com.skellyco.hito.core.util.security;

public class EncryptionResult {

    private byte[] encryptedData;
    private byte[] iv;

    public EncryptionResult(byte[] encryptedData, byte[] iv)
    {
        this.encryptedData = encryptedData;
        this.iv = iv;
    }

    public byte[] getEncryptedData()
    {
        return encryptedData;
    }

    public byte[] getIv()
    {
        return iv;
    }
}
