package chat.entities;

import chat.Models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientTwo {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Socket connectToServer(String serverAddress, int port) throws IOException {
        System.out.println("Connecting to server: " + serverAddress + ":" + port);

        try {
            socket = new Socket(serverAddress, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Successfully connected to server!");
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sendMessageToServer(String content) throws IOException {
        Random random = new Random();
        Message message = new Message(random.nextInt(1001), content);
        objectOutputStream.writeObject(message);
    }

    public void listenForServerMessages() {
        new Thread(() -> {
            try {
                while (true) {
                    Message serverMessage = (Message) objectInputStream.readObject();
                    System.out.println("ClientTwo message: " + serverMessage.getBody());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    objectOutputStream.close();
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ClientTwo clientTwo = new ClientTwo();
        Socket clientServerConnection = clientTwo.connectToServer("localhost", 8081);

        if (clientServerConnection != null) {
            clientTwo.listenForServerMessages();

            while (true) {
                System.out.println("Enter your message");
                String messageContent = scanner.nextLine();
                clientTwo.sendMessageToServer(messageContent);
            }
        }
    }
}
