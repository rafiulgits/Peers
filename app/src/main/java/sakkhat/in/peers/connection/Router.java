package sakkhat.in.peers.connection;

import android.os.Handler;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import sakkhat.in.peers.pages.Index;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class Router {

    public static class Receiver implements Listener, Runnable {
        private static final String TAG = "router_sender";
        private Thread engine;
        private Handler handler;



        private Receiver(Handler handler){
            this.handler = handler;
        }

        public static Receiver init(Handler handler){
            return new Receiver(handler);
        }

        @Override
        public void run() {
            if(! ConnectionManager.isEastablished()){
                handler.obtainMessage(SOCKET_ERROR);
                return;
            }
            Socket socket = ConnectionManager.getSocket();

            try{
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int command = dataInputStream.readInt();
                while (socket != null || socket.isConnected()){
                    if(command == Index.GOTO_FILE_SHARING){
                        handler.obtainMessage(Index.GOTO_FILE_SHARING).sendToTarget();
                    }
                    else if(command == Index.GOTO_CONTROL_SHARING) {
                        handler.obtainMessage(Index.GOTO_CONTROL_SHARING).sendToTarget();
                    }
                    continue;
                }
            } catch(IOException ex){
                handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            }

        }

        @Override
        public void execute() {
            engine = new Thread(this);
            engine.start();
        }

        @Override
        public void terminate() {
            try{
                engine.interrupt();
            } catch (Exception ex){
                Log.e(TAG, ex.toString());
            }
        }
    }


    public static class Sender implements Listener, Runnable{
        private static final String TAG = "router_sender";
        private Thread engine;
        private Handler handler;
        private volatile int command;

        private Sender(Handler handler, int command){
            this.handler = handler;
            this.command = command;
        }

        public static Sender init(Handler handler, int command){
            return new Sender(handler,command);
        }

        @Override
        public void run() {
            if(!ConnectionManager.isEastablished()){
                return;
            }
            Socket socket = ConnectionManager.getSocket();
            try{
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeInt(command);

            } catch (IOException ex){
                Log.e(TAG, "something went wrong");
            }
        }

        @Override
        public void execute() {
            engine = new Thread(this);
            engine.start();
        }

        @Override
        public void terminate() {
            try{
                engine.interrupt();
            } catch (Exception ex){
                Log.e(TAG, ex.toString());
            }
        }
    }

}
