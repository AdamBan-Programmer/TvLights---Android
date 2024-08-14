package com.example.tvlights.Network;

import java.io.Serializable;
import java.net.InetAddress;

public class Client implements Serializable {

    private String ipAddress;
    private int port;

    public Client() {
    }

    public Client(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
