package com.example.tvlights.Service;

import com.example.tvlights.Network.Connection;
import com.example.tvlights.Settings.AppSettings;
import com.example.tvlights.Utils.JsonParser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionService {

    static JsonParser jsonController = new JsonParser();

    public static void startConnection() throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Connection connection = Connection.getInstance();
                    if (connection.getClientSocket() == null) {
                        String ip = AppSettings.getInstance().getClientAddress().getIpAddress();
                        int port = AppSettings.getInstance().getClientAddress().getPort();

                        connection.setClientSocket(
                                new Socket(ip, port));
                        connection.setOutputStream(
                                new PrintWriter(connection.getClientSocket().getOutputStream(), true));
                        connection.setInputStream(
                                new BufferedReader(new InputStreamReader(connection.getClientSocket().getInputStream())));
                        connection.setConnectionStatus(true);
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

    //to leds controller (raspberry pi)
    public void sendLedsColor(String json) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Connection connection = Connection.getInstance();
                    if (connectionIsAvailable(connection)) {
                        DataOutputStream outputStream = new DataOutputStream(connection.getClientSocket().getOutputStream());
                        outputStream.write(json.getBytes());
                        outputStream.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private boolean connectionIsAvailable(Connection connection) throws NullPointerException, InterruptedException, SocketException {
        boolean connectionNotSet =
                connection == null ||
                        connection.getInputStream() == null ||
                        connection.getOutputStream() == null ||
                        connection.getClientSocket() == null;

        if (connectionNotSet) {
            startConnection();
        }
        checkSocketStatus(connection);
        if(!connection.getConnectionStatus())
        {
            stopConnection();
            throw new SocketException();
        }
        return !connectionNotSet;
    }

    //trying to read data from socket, if error occurred it means the connection is closed
    private static void checkSocketStatus(Connection connection) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    DataInputStream dis = new DataInputStream(connection.getClientSocket().getInputStream());
                    dis.read();
                } catch (SocketException e) {
                    connection.setConnectionStatus(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void stopConnection(){
        try {
            Connection connection = Connection.getInstance();
            sendDisconnectMessage(connection);
            connection.getInputStream().close();
            connection.getOutputStream().close();
            connection.getClientSocket().close();
        }
        catch (IOException | NullPointerException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally {
            Connection.reset();
        }
    }

    //sends 'close' to leds controller
    private static void sendDisconnectMessage(Connection connection) throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    String disconnectJSON = prepareDisconnectData();
                    DataOutputStream outputStream = new DataOutputStream(connection.getClientSocket().getOutputStream());
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

    private static String prepareDisconnectData()
    {
        String closeString = "close";
        return jsonController.toJsonFormat(closeString);
    }
}
