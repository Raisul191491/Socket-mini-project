package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ReadWriteHandler implements Runnable{
    public Socket socket;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public String clientUserName;

    public ReadWriteHandler(Socket socket) {
        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        String clientMsg;
        while (socket.isConnected()){
            try {
                clientMsg = dataInputStream.readUTF();
                System.out.println(clientMsg);
            } catch (IOException e){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
