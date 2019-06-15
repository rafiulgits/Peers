package sakkhat.in.peers.model;

/**
 * Created by Rafiul Islam on 17-Apr-19.
 */

public class FileIO {
    public static final int SENDING = 1;
    public static final int RECEIVING = 2;
    private String name;
    private int type;
    private boolean streaming;

    private FileIO(String name, int type){
        this.name = name;
        this.type = type;
        streaming = true;
    }

    public static FileIO init(String name, int type){
        return new FileIO(name, type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public int getType() {
        return type;
    }
}
