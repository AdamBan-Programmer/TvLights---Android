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

    static JsonParser jsonController = new JsonParser();

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

    public static void startConnection() throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    if (instance == null) {
                        String ip = AppSettings.getInstance().getClientAddress().getIpAddress();
                        int port = AppSettings.getInstance().getClientAddress().getPort();

                        getInstance().setClientSocket(
                                new Socket(ip, port));
                        getInstance().setOutputStream(
                                new PrintWriter(instance.getClientSocket().getOutputStream(), true));
                        getInstance().setInputStream(
                                new BufferedReader(new InputStreamReader(instance.getClientSocket().getInputStream())));
                        getInstance().setConnectionStatus(true);
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

    public static boolean connectionIsAvailable() throws NullPointerException, InterruptedException, SocketException {
        boolean connectionNotSet =
                instance == null ||
                instance.getInputStream() == null ||
                        instance.getOutputStream() == null ||
                        instance.getClientSocket() == null;

        if (connectionNotSet) {
            startConnection();
        }
        checkSocketStatus();
        if(!getInstance().getConnectionStatus())
        {
            stopConnection();
            throw new SocketException();
        }
        return !connectionNotSet;
    }

    //trying to read data from socket, if error occurred it means the connection is closed
    private static void checkSocketStatus() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataInputStream dis = new DataInputStream(instance.getClientSocket().getInputStream());
                    dis.read();
                } catch (SocketException e) {
                    getInstance().setConnectionStatus(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    //to leds controller (raspberry pi)
    public static void sendLedsColor(String json) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataOutputStream outputStream = new DataOutputStream(instance.getClientSocket().getOutputStream());
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
    private static void sendDisconnectMessage(String disconnectJSON) throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataOutputStream outputStream = new DataOutputStream(instance.getClientSocket().getOutputStream());
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

    public static void stopConnection(){
        try {
            String disconnectJSON = prepareDisconnectData();
            sendDisconnectMessage(disconnectJSON);
            instance.getInputStream().close();
            instance.getOutputStream().close();
            instance.getClientSocket().close();
        }
        catch (IOException | NullPointerException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally {
            instance = null;
        }
    }

    private static String prepareDisconnectData()
    {
        String closeString = "close";
        return jsonController.toJsonFormat(closeString);
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
