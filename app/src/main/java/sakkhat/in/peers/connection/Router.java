package sakkhat.in.peers.connection;

import android.os.Handler;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class Router {

    public static class Receiver implements Listener, Runnable {
        private static final String TAG = "router_receiver";
        private Thread engine;
        private Handler handler;

        private FileReceiver fileReceiver;


        private Receiver(Handler handler) {
            this.handler = handler;
            fileReceiver = FileReceiver.init(handler);
        }

        public static Receiver init(Handler handler) {
            return new Receiver(handler);
        }

        @Override
        public void run() {
            if (!ConnectionManager.isEastablished()) {
                handler.obtainMessage(SOCKET_ERROR);
                return;
            }
            Socket socket = ConnectionManager.getSocket();

            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                short command = dataInputStream.readShort();
                if (command == FILE_RECEIVE_REQUEST) {
                    FileReceiver fileReceiverClone = (FileReceiver) fileReceiver.clone();
                    fileReceiverClone.execute();
                    fileReceiverClone.join();
                    handler.obtainMessage(RECEIVING_COMPLETED).sendToTarget();
                }

            } catch (IOException ex) {
                handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            }

        }

        @Override
        public void execute() {
            engine = new Thread(this);
            engine.start();
        }

        @Override
        public boolean isExecuting() {
            if (engine.isAlive() && !engine.isInterrupted()) {
                return true;
            }
            return false;
        }

        @Override
        public void terminate() {
            try {
                engine.interrupt();
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
    }
}
