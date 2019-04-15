package sakkhat.in.peers.connection;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class P2P {
    private static final int PORT = 7878;

    public static class Server implements Runnable, Listener {

        private static final String TAG ="p2p_server";

        private Thread engine;
        private Handler handler;

        private Server(Handler handler){
            this.handler = handler;
        }

        public static Server init(Handler handler){
            return new Server(handler);
        }

        @Override
        public void run() {
            try {
                Log.w(TAG, "waiting for client");
                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
                ConnectionManager.setSocket(socket);
                handler.obtainMessage(SOCKET_ESTABLISHED).sendToTarget();
            } catch (IOException ex){
                Log.e(TAG, ex.toString());
                handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            }
        }

        @Override
        public void execute() {
            engine = new Thread(this);
            engine.start();
        }

        @Override
        public void terminate(){

        }
    }

    public static class Client implements Runnable,Listener {

        private static final String TAG = "p2p_client";

        private Thread engine;
        private InetAddress host;
        private Handler handler;

        private Client(InetAddress host, Handler handler) {
            this.host = host;
            this.handler = handler;
        }

        public static Client init(InetAddress host, Handler handler){
            return new Client(host, handler);
        }

        @Override
        public void run() {
            try {
                Log.w(TAG, "requesting to the server");
                Socket socket = new Socket(host, PORT);
                ConnectionManager.setSocket(socket);
                handler.obtainMessage(SOCKET_ESTABLISHED).sendToTarget();
            } catch (IOException ex){
                Log.e(TAG, ex.toString());
                handler.obtainMessage(SOCKET_ERROR).sendToTarget();
            }
        }

        @Override
        public void execute() {
            engine = new Thread(this);
            engine.start();
        }

        @Override
        public void terminate(){

        }
    }
}