package com.example.tvlights.File;
import android.os.Environment;

import java.io.IOException;

public class FileScanner {
    private static final String APP_NAME = "TvLights";
    private static final String SETTINGS_FILE = "SettingsFile.txt";

    public String getPathToSettingsFile() {
        return "/Android/"+APP_NAME+"/"+SETTINGS_FILE;
    }


    public void checkSettingsFolderExists() throws IOException {
            String directoryPath = Environment.getExternalStorageDirectory() + "/Android/"+APP_NAME;
            java.io.File settingsDirectory = new java.io.File(directoryPath);
            java.io.File settingsFile = new java.io.File(directoryPath + "/" + SETTINGS_FILE);

            if (settingsDirectory.exists() == false) {
                settingsDirectory.mkdirs();
            }
            if (settingsFile.exists() == false) {
                settingsFile.createNewFile();
            }
        }
}
