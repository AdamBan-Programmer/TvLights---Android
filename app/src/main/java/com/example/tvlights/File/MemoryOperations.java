package com.example.tvlights.File;

import android.os.Environment;


import com.example.tvlights.Settings.AppSettings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MemoryOperations {

    FileScanner fileScannerController = new FileScanner();

    // Saves settings into a file
    public void SerializeObjectToFile(AppSettings settings) throws Exception {
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + fileScannerController.getPathToSettingsFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(settings);
    }

    //load settings from file
    public void ReadSerializedObject() throws Exception {
        FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + fileScannerController.getPathToSettingsFile());
        ObjectInputStream ois = new ObjectInputStream(fis);
        AppSettings.updateAppSettings((AppSettings) ois.readObject());
    }
}
