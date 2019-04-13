package sakkhat.in.peers.connection;

import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class SocketHandler {

    private static Socket socket;

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }

    public static synchronized boolean isEastablished(){
        if(SocketHandler.socket != null)
            return true;
        return false;
    }

    public static synchronized Socket getSocket (){
        return SocketHandler.socket;
    }
}
