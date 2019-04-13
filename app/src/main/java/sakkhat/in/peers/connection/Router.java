package sakkhat.in.peers.connection;

import android.os.Handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class Router implements Listener, Runnable {

    private Thread engine;
    private Handler handler;
    private FileReceiver fileReceiver;

    private Router(Handler handler){
        this.handler = handler;
        fileReceiver = FileReceiver.init(handler);
    }

    public Router init(Handler handler){
        return new Router(handler);
    }

    @Override
    public void run() {
        if(! ConnectionManager.isEastablished()){
            handler.obtainMessage(SOCKET_ERROR);
            return;
        }
        Socket socket = ConnectionManager.getSocket();

        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            short command;
            while (socket != null || socket.isConnected()){

                command = dataInputStream.readShort();
                switch (command){
                    case FILE_RECEIVE_REQUEST:
                        FileReceiver fileReceiverClone = (FileReceiver) fileReceiver.clone();
                        if(fileReceiverClone != null){
                            fileReceiverClone.execute();
                        }
                        else{
                            handler.obtainMessage(ERROR);
                        }break;
                    default:continue;
                }


            }
        } catch (IOException ex){
            handler.obtainMessage(SOCKET_ERROR);
        }
    }

    @Override
    public void execute() {
        engine = new Thread(this);
        engine.start();
    }
}
