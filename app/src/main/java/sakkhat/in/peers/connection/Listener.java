package sakkhat.in.peers.connection;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public interface Listener {
    public static final int SOCKET_ERROR = 1;
    public static final int FILE_RECEIVE_REQUEST = 2;
    public static final int FILE_RECEIVING_PROGRESS = 3;
    public static final int FILE_RECEIVED = 4;
    public static final int FILE_SENDING_PROGRESS = 5;
    public static final int FILE_SENT = 6;

    public static final int FILE_INFO = 7;
    public static final int SENDING_QUEUE_CLEARED = 8;
    public static final int ERROR = 0;
    public static final int SOCKET_ESTABLISHED = 9;
    public static final int SUCCESS = 10;

    public void execute();
    public void terminate();
}
