package com.example.tvlights.File;

import android.os.Environment;


import com.example.tvlights.Settings.AppSettings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class MemoryOperations {

    FileScanner fileScannerController = new FileScanner();
    FileEncryption encryptionController = new FileEncryption();

    // Saves settings into a file
    public void SerializeObjectToFile(AppSettings SettingsObject) throws Exception {
        fileScannerController.checkSettingsFolderExists();
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + fileScannerController.getPathToSettingsFile());
        encryptionController.EncryptObject(SettingsObject, fos);
    }

    //load settings from file
    public void ReadSerializedObject() throws Exception {
            fileScannerController.checkSettingsFolderExists();
            SecretKeySpec sks = new SecretKeySpec(encryptionController.getKey(), encryptionController.getTransformation());
            Cipher cipher = Cipher.getInstance(encryptionController.getTransformation());
            cipher.init(Cipher.DECRYPT_MODE, sks);

            FileInputStream istream = new FileInputStream(Environment.getExternalStorageDirectory() + fileScannerController.getPathToSettingsFile());
            CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
            ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
            SealedObject sealedObject = (SealedObject) inputStream.readObject();
            AppSettings.updateAppSettings((AppSettings) sealedObject.getObject(cipher));
    }
}
