package chat.entities;

import chat.Models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {
    public Socket connectToServer(String serverAddress, int port) throws IOException{
        System.out.println("Connecting to server: " + serverAddress + ":" + port);

        try {
            Socket socket = new Socket(serverAddress,port);
            System.out.println("Successfully connected to server!");
            return socket;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void sendMessageToServer(Socket socket,String content) throws IOException{
        Random random = new Random();
        Message message = new Message(random.nextInt(1001),content);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(message);

    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        Socket clientServerConnection = client.connectToServer("localhost",8081);

        while (true){
            System.out.println("Enter you message");
            String messageContent = scanner.nextLine();
            client.sendMessageToServer(clientServerConnection, messageContent);
        }
    }

}
