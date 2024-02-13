package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static List<Socket>clients = new ArrayList<>();
  
    public static void main(String[] args) {
            try {
                server = new ServerSocket(3345);
                System.out.println("Server start!");
                while (true){
                    clientSocket = server.accept();
                    System.out.println("Подключение установлено с " + clientSocket);
                    clients.add(clientSocket);
                    Thread thread = new Thread(()->HandlerClient(clientSocket));
                    thread.start();
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    public static void BroadCastMessage(String message, Socket sender){
        for(Socket client: clients){
            if(client != sender){
                try {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(message);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void HandlerClient(Socket clientSocket){
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
            String message;
            while((message = bufferedReader.readLine()) != null){
                if (message.isEmpty()) {
                    break;
                }
                System.out.println("message received from client: " + message);
                BroadCastMessage(message, clientSocket);
            }
        }
        catch (IOException e){
            e.getMessage();
        }
    }
}
