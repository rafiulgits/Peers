package sakkhat.in.peers.connection;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class FileQueue {
    private ArrayList<File> queue;

    private FileQueue(){
        queue = new ArrayList<>();
    }

    public static FileQueue init(){
        return new FileQueue();
    }

    public void enqueue(File file){
        queue.add(file);
    }

    public File dequeue(){
        if(! queue.isEmpty()){
            File file = queue.get(queue.size()-1);
            queue.remove(queue.size()-1);
            return file;
        }
        return null;
    }

    public boolean hasItem(){
        return queue.isEmpty();
    }
}
