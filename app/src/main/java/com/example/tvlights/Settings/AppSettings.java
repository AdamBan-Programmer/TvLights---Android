package com.example.tvlights.Settings;


import com.example.tvlights.Network.Client;

import java.io.Serializable;

public class AppSettings implements Serializable {

    private Client clientAddress;

    static AppSettings currentAppSettings = null;

    public AppSettings()
    {

    }

    public AppSettings(Client clientAddress) {
        this.clientAddress = clientAddress;
    }

    //singleton
    public void setCurrentAppSettings(AppSettings newSettings)
    {
        currentAppSettings = newSettings;
    }

    public AppSettings getCurrentAppSettings()
    {
        if(currentAppSettings == null)
        {
            currentAppSettings = new AppSettings(new Client("192.168.0.127",0));
        }
        return currentAppSettings;
    }

    public Client getClientAddress() {
        return this.clientAddress;
    }

    public void setClientAddress(Client clientAddress) {
        this.clientAddress = clientAddress;
    }
}

