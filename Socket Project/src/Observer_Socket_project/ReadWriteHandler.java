package Observer_Socket_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ReadWriteHandler implements Runnable, Observable{
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
        String clientMsg = null, newClientMsg;
        while (socket.isConnected()){
            try {
                newClientMsg = dataInputStream.readUTF();
                if(!newClientMsg.equalsIgnoreCase(clientMsg)){
                    notifyAll(newClientMsg);
                    clientMsg = newClientMsg;
                }
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

    @Override
    public void notifyAll(String message) throws IOException {
        for (int i = 0; i< ObservableServer.readWriteHandlerArrayList.size(); i++){
            ReadWriteHandler name = ObservableServer.readWriteHandlerArrayList.get(i);
            if (!name.clientUserName.equalsIgnoreCase(this.clientUserName) && !name.socket.isClosed()) {
                name.dataOutputStream.writeUTF(message);
            }
            if(name.socket.isClosed()){
                ObservableServer.readWriteHandlerArrayList.remove(name);
            }
        }
    }
}
