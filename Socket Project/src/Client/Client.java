package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public Socket socket;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public BufferedWriter bufferedWriter;
    public String userName;

    public Client(Socket socket, String userName) throws IOException {
        this.socket = socket;
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        this.userName = userName;
    }

    public void sendMessage(){
        try {
            Scanner scanner = new Scanner(System.in);
            dataOutputStream.writeUTF(userName);
            String msgFromMe;

            while(socket.isConnected())
            {
                msgFromMe = scanner.nextLine();
                dataOutputStream.writeUTF(userName + " : " + msgFromMe);
                dataOutputStream.flush();
            }
        }catch (IOException q) {
            q.printStackTrace();
        }
    }

    public void messageListener(){
        new Thread(() -> {
            String msgReceived;

            while (socket.isConnected()){
                try {
                    msgReceived = dataInputStream.readUTF();
                    System.out.println(msgReceived);
                }catch (IOException q){
                    q.printStackTrace();
                }
            }
        }).start();
    }


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username : ");
        String user = scanner.nextLine();
        Socket socket = new Socket("localhost", 5566);
        Client client = new Client(socket, user);
        client.messageListener();
        client.sendMessage();
    }
}

