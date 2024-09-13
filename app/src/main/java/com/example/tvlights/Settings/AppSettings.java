package com.example.tvlights.Settings;


import com.example.tvlights.Network.Client;

import java.io.Serializable;

public final class AppSettings implements Serializable {

    private Client clientAddress;

    private static AppSettings currentAppSettings;

    private AppSettings(Client clientAddress) {
        this.clientAddress = clientAddress;
    }

    //singleton
    public static AppSettings getInstance()
    {
        if(currentAppSettings == null)
        {
            currentAppSettings = new AppSettings(new Client("192.168.0.127",0));
        }
        return currentAppSettings;
    }

    public static void updateAppSettings(AppSettings newSettings)
    {
        currentAppSettings = newSettings;
    }

    public Client getClientAddress() {
        return this.clientAddress;
    }

}

