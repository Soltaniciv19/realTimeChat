package chat.entities;

import chat.Models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void runServer(int port) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server running on port: " + port);

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                Message clientRequest = (Message) objectInputStream.readObject();
                System.out.println(clientRequest.getBody());
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        runServer(8081);
    }
}
