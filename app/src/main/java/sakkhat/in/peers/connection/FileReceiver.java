package sakkhat.in.peers.connection;

import android.os.Handler;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class FileReceiver implements Runnable, IO {

    private Thread engine;
    private Handler handler;

    private FileReceiver(Handler handler){
        this.handler = handler;
    }

    public FileReceiver init(Handler handler){
        return new FileReceiver(handler);
    }

    @Override
    public void run() {

    }

    @Override
    public void execute() {
        engine = new Thread(this);
        engine.start();
    }
}
