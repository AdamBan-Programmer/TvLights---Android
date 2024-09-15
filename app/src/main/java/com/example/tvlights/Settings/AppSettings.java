package com.example.tvlights.Settings;


import com.example.tvlights.Network.Client;

import java.io.Serializable;

public final class AppSettings implements Serializable {

    private static AppSettings instance;
    private Client clientAddress;
    private AppSettings(Client clientAddress) {
        this.clientAddress = clientAddress;
    }

    //singleton
    public static AppSettings getInstance()
    {
        if(instance == null)
        {
            instance = new AppSettings(new Client("192.168.0.127",0));
        }
        return instance;
    }

    public static void updateAppSettings(AppSettings newSettings)
    {
        instance = newSettings;
    }

    public Client getClientAddress() {
        return this.clientAddress;
    }

}

