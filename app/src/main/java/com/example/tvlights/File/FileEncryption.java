package com.example.tvlights.File;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryption extends FileScanner {

    private byte[] key;
    private String transformation;

    static FileEncryption encryptor = new FileEncryption("79857987498379859760967097898622".getBytes(), "AES/ECB/PKCS5Padding");

    public FileEncryption() {

    }

    public FileEncryption(byte[] key, String transformation) {
        this.key = key;
        this.transformation = transformation;
    }

    public byte[] EncryptObject(Serializable object, OutputStream ostream) throws Exception {
        byte[] dataEncrypted = null;
        SecretKeySpec sks = new SecretKeySpec(encryptor.key, encryptor.transformation);

        // Create cipher
        Cipher cipher = Cipher.getInstance(encryptor.transformation);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        SealedObject sealedObject = new SealedObject(object, cipher);

        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
        outputStream.writeObject(sealedObject);
        outputStream.close();
        return dataEncrypted;
}

    public byte[] getKey() {
        return encryptor.key;
    }

    public String getTransformation() {
        return encryptor.transformation;
    }
}
