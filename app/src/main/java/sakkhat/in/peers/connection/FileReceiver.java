package sakkhat.in.peers.connection;

import android.os.Environment;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class FileReceiver implements Listener, Cloneable {
    private static final String PATH = Environment.getExternalStorageDirectory()+"/p2p";

    private Handler handler;

    private FileReceiver(Handler handler){
        this.handler = handler;
    }

    public static FileReceiver init(Handler handler){
        return new FileReceiver(handler);
    }

    @Override
    public void execute() {
        if(! ConnectionManager.isEastablished()){
            handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            return;
        }
        Socket socket = ConnectionManager.getSocket();
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(dataInputStream);

            String fileName;
            long len, load;
            int readLen;

            while (true){
                fileName =  dataInputStream.readUTF();
                len = dataInputStream.readLong();
                load = 0;

                byte[] rawData = new byte[64*1024];
                File dir = new File(PATH);
                if(!dir.exists()){
                    dir.mkdir();
                }

                File file = new File(dir+"/"+fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while((readLen = bufferedInputStream.read(rawData, 0, rawData.length))!= -1){
                    load += readLen;
                    fileOutputStream.write(rawData, 0, readLen);
                    // TODO:
                    // receiving progress

                    if(load ==len){
                        handler.obtainMessage(FILE_RECEIVED).sendToTarget();
                        break;
                    }
                }
                fileOutputStream.flush();
                fileOutputStream.close();

                short next = dataInputStream.readShort();
                if(next == FILE_RECEIVE_REQUEST){
                    continue;
                }
                else{
                    break;
                }
            }


        } catch (IOException ex){
            handler.obtainMessage(SOCKET_ERROR).sendToTarget();
        }
    }

    @Override
    public Object clone(){
        try{
            return super.clone();
        } catch (CloneNotSupportedException ex){
            return null;
        }
    }
}
