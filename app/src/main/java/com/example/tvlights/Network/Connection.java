package com.example.tvlights.Network;

import com.example.tvlights.Settings.AppSettings;
import com.example.tvlights.Utils.JsonParser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

public final class Connection extends Client implements Serializable {

    private static Connection instance;

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private boolean connectionStatus;
    private Connection(String ipAddress, int port, Socket clientSocket, PrintWriter outputStream, BufferedReader inputStream, boolean connectionStatus) {
        super(ipAddress, port);
        this.clientSocket = clientSocket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.connectionStatus = connectionStatus;
    }

    public static void reset()
    {
        instance = null;
    }

    public static Connection getInstance() {
        if(instance == null)
        {
            instance = new Connection("",0,null,null,null,false);
        }
        return instance;
    }

    public void setCurrentConnection(Connection newCurrentConnection) {
        instance = newCurrentConnection;
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public PrintWriter getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(PrintWriter out) {
        this.outputStream = out;
    }

    public BufferedReader getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(BufferedReader inputStream) {
        this.inputStream = inputStream;
    }

    public boolean getConnectionStatus() {
        return this.connectionStatus;
    }
    public void setConnectionStatus(boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}
