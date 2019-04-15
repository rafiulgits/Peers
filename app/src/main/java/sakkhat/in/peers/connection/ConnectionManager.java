package sakkhat.in.peers.connection;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class ConnectionManager {

    private static final String TAG = "connection_manager";

    private static Socket socket;
    private static WifiP2pManager p2pManager;
    private static WifiP2pManager.Channel p2pChannel;

    public static synchronized void setSocket(Socket socket){
        ConnectionManager.socket = socket;
    }

    public static synchronized boolean isEastablished(){
        if(ConnectionManager.socket != null)
            return true;
        return false;
    }

    public static synchronized Socket getSocket (){
        return ConnectionManager.socket;
    }

    public static synchronized void setP2pManager(WifiP2pManager p2pManager){
        ConnectionManager.p2pManager = p2pManager;
    }

    public static synchronized WifiP2pManager getP2pManager(){
        return ConnectionManager.p2pManager;
    }

    public static synchronized void setP2pChannel(WifiP2pManager.Channel p2pChannel){
        ConnectionManager.p2pChannel = p2pChannel;
    }

    public static synchronized WifiP2pManager.Channel getP2pChannel(){
        return ConnectionManager.p2pChannel;
    }

    public static void disconnect(WifiP2pManager.ActionListener actionListener){
        socket = null;
        p2pManager.removeGroup(p2pChannel, actionListener);
    }
}
