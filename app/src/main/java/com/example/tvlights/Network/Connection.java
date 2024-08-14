package com.example.tvlights.Network;

import com.example.tvlights.Settings.AppSettings;
import com.example.tvlights.Utils.JsonParser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

public class Connection extends Client implements Serializable {

    AppSettings appSettingsController = new AppSettings();
    JsonParser jsonController = new JsonParser();

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private boolean connectionStatus;

    private static Connection currentConnection = null;

    public Connection() {
    }

    public Connection(String ipAddress, int port, Socket clientSocket, PrintWriter outputStream, BufferedReader inputStream, boolean connectionStatus) {
        super(ipAddress, port);
        this.clientSocket = clientSocket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.connectionStatus = connectionStatus;
    }

    public void startConnection() throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    if (currentConnection == null) {
                        getCurrentConnection().setClientSocket(
                                new Socket(currentConnection.getIpAddress(), currentConnection.getPort()));
                        getCurrentConnection().setOutputStream(
                                new PrintWriter(currentConnection.getClientSocket().getOutputStream(), true));
                        getCurrentConnection().setInputStream(
                                new BufferedReader(new InputStreamReader(currentConnection.getClientSocket().getInputStream())));
                        getCurrentConnection().setConnectionStatus(true);
                    }
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join(1000);
    }

    public boolean connectionIsAvailable() throws NullPointerException, InterruptedException, SocketException {
        boolean connectionNotSet =
                currentConnection == null ||
                currentConnection.getInputStream() == null ||
                        currentConnection.getOutputStream() == null ||
                        currentConnection.getClientSocket() == null;

        if (connectionNotSet) {
            startConnection();
        }
        checkSocketStatus();
        if(!getCurrentConnection().getConnectionStatus())
        {
            stopConnection();
            throw new SocketException();
        }
        return !connectionNotSet;
    }

    //trying to read data from socket, if error occurred it means the connection is closed
    private void checkSocketStatus() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataInputStream dis = new DataInputStream(currentConnection.getClientSocket().getInputStream());
                    dis.read();
                } catch (SocketException e) {
                    getCurrentConnection().setConnectionStatus(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    //to leds controller (raspberry pi)
    public void sendLedsColor(String json) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataOutputStream outputStream = new DataOutputStream(currentConnection.getClientSocket().getOutputStream());
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    //sends 'close' to leds controller
    private void sendDisconnectMessage(String disconnectJSON) throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataOutputStream outputStream = new DataOutputStream(currentConnection.getClientSocket().getOutputStream());
                    outputStream.write(disconnectJSON.getBytes());
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join();
    }

    public void stopConnection(){
        try {
            String disconnectJSON = prepareDisconnectData();
            sendDisconnectMessage(disconnectJSON);
            currentConnection.getInputStream().close();
            currentConnection.getOutputStream().close();
            currentConnection.getClientSocket().close();
        }
        catch (IOException | NullPointerException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally {
            currentConnection = null;
        }
    }

    private String prepareDisconnectData()
    {
        String closeString = "close";
        return jsonController.toJsonFormat(closeString);
    }

    public Connection getCurrentConnection() {
        if(currentConnection == null)
        {
            String ipAddress = appSettingsController.getCurrentAppSettings().getClientAddress().getIpAddress();
            int port = appSettingsController.getCurrentAppSettings().getClientAddress().getPort();
            currentConnection = new Connection(ipAddress,port,null,null,null,false);
        }
        return currentConnection;
    }

    public void setCurrentConnection(Connection newCurrentConnection) {
        currentConnection = newCurrentConnection;
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
