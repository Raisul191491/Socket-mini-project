package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocket;
    ArrayList<ReadWriteHandler> readWriteHandlerArrayList = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() throws IOException {
        new Thread(() ->  {
            try {
                while(!serverSocket.isClosed())
                {
                    Socket client = serverSocket.accept();
                    ReadWriteHandler clientThread = new ReadWriteHandler(client);
                    DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                    clientThread.clientUserName = dataInputStream.readUTF();
                    readWriteHandlerArrayList.add(clientThread);
                    Thread level = new Thread(clientThread);
                    level.start();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
        while (true){
            //System.out.println("Server -> ");
            String msg = bufferedReader.readLine();
            sendMessage(msg);
        }
    }

    private void sendMessage(String msg) throws IOException {
        if(readWriteHandlerArrayList.isEmpty()){
            System.out.println("There's no one to send msg to :(");
        }else{
            System.out.println("Who do u want to send the msg to?");
            for (int i=0; i<readWriteHandlerArrayList.size(); i++){
                ReadWriteHandler name = readWriteHandlerArrayList.get(i);
                if(name.socket.isClosed()){
                    continue;
                }else {
                    System.out.println(i + 1 + ". " + name.clientUserName);
                }
            }
            int inp = scanner.nextInt();
            ReadWriteHandler name = readWriteHandlerArrayList.get(inp-1);
            name.dataOutputStream.writeUTF(msg);
            name.dataOutputStream.flush();
        }
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5566);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}

