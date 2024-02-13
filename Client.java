package org.example;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static final String SERVER_HOST = "localhost";
    public static final String SERVER_PORT = "3345";
    private PrintWriter out;
    private BufferedReader in;


    public static void main(String[] args){
        try (Socket socket = new Socket("localhost", 3345)){
            System.out.println("Connection to server successful");
            Thread receiveThread = new Thread(new ReceiveThread(socket));
            receiveThread.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String userInput = bufferedReader.readLine();
            while(userInput != null){
                out.println(userInput);
                userInput = bufferedReader.readLine();
                if (userInput.isEmpty()) {
                    break;
                }
            }
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static class ReceiveThread implements Runnable{

        public static String name;
        public static Socket socket;
        public ReceiveThread(Socket socket){
            this.socket = socket;
        };

        @Override
        public void run(){
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Message from server: " + message);
                    break;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("%s fiished... \n", Thread.currentThread().getName());
        }
    }
}
