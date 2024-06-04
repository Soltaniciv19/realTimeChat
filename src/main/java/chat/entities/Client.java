package chat.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    public Socket connectToServer(String serverAddress, int port) throws IOException{
        System.out.println("Connecting to server: " + serverAddress + ":" + port);

        try (Socket socket = new Socket(serverAddress,port)){
            System.out.println("Successfully connected to server!");
            return socket;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connectToServer("localhost",8081);
    }
}
