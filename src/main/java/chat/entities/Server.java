package chat.entities;

import chat.Models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static void runServer(int port) throws IOException {
        List<ClientHandler> clientHandlers = new ArrayList<>();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server running on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientHandlers);
                clientHandlers.add(clientHandler);
                System.out.println("Client connected");

                new Thread(clientHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        runServer(8081);
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private List<ClientHandler> clientHandlers;

    public ClientHandler(Socket socket, List<ClientHandler> clientHandlers) throws IOException {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            Message message;
            while ((message = (Message) objectInputStream.readObject()) != null) {
                System.out.println("Sending message to " + socket.getInetAddress() + ":" + socket.getPort());
                broadcastMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                objectInputStream.close();
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(Message message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                try {
                    clientHandler.objectOutputStream.writeObject(message);
                    clientHandler.objectOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
