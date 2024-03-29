package sakkhat.in.peers.connection;

import android.os.Handler;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class FileSender implements Engine, Runnable, Listener {

    private Thread engine;
    private Handler handler;
    private FileQueue fileQueue;

    private FileSender(Handler handler, FileQueue fileQueue){
        this.handler = handler;
        this.fileQueue = fileQueue;
    }

    public static FileSender init(Handler handler, FileQueue fileQueue){
        return new FileSender(handler, fileQueue);
    }

    @Override
    public void run() {
        if(!ConnectionManager.isEastablished()){
            handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            return;
        }
        Socket socket = ConnectionManager.getSocket();

        while (fileQueue.hasItem()){
            try{
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(dataOutputStream);

                File file = fileQueue.dequeue();

                dataOutputStream.writeShort(FILE_RECEIVE_REQUEST);
                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.writeLong(file.length());

                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] kb64 = new byte[1020*64];
                int readLength; long available = file.length();

                while((readLength = fileInputStream.read(kb64, 0, kb64.length)) > 0){
                    available -= readLength;
                    bufferedOutputStream.write(kb64, 0, readLength);
                    // TODO:
                    // update progress
                    // handler.obtainMessage(FILE_SENDING_PROGRESS)

                    if(available <= 0){
                        bufferedOutputStream.flush();
                        fileInputStream.close();
                        break;
                    }
                }
                handler.obtainMessage(FILE_SENT);

                if(!fileQueue.hasItem()){
                    dataOutputStream.writeShort(SENDING_QUEUE_CLEARED);
                }

            } catch (IOException ex){
                handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            }
        }
        handler.obtainMessage(SENDING_QUEUE_CLEARED);

    }

    public void join(){
        try {
            engine.join();
        } catch (InterruptedException e) {
            handler.obtainMessage(ERROR).sendToTarget();
        }
    }

    @Override
    public void terminate(){
        engine.interrupt();
    }

    @Override
    public boolean isExecuting(){
        if(engine.isAlive() && !engine.isInterrupted()){
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        engine = new Thread(this);
        engine.start();
    }
}
