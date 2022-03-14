package Observer_Socket_project;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ObservableServer implements Observable{

    private final ServerSocket serverSocket;
    public static ArrayList<ReadWriteHandler> readWriteHandlerArrayList = new ArrayList<>();


    public ObservableServer(ServerSocket serverSocket) {
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
                    String alert = clientThread.clientUserName + " has joined the chat";
                    notifyAll(alert);
                    readWriteHandlerArrayList.add(clientThread);
                    Thread level = new Thread(clientThread);
                    level.start();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }



    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5566);
        ObservableServer server = new ObservableServer(serverSocket);
        server.startServer();
    }

    @Override
    public void notifyAll(String message) throws IOException {
        for (int i=0; i<readWriteHandlerArrayList.size(); i++){
            ReadWriteHandler name = readWriteHandlerArrayList.get(i);
            name.dataOutputStream.writeUTF(message);
        }
    }
}
