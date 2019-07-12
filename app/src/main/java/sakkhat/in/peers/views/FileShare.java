package sakkhat.in.peers.views;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import lib.folderpicker.FolderPicker;
import sakkhat.in.peers.R;
import sakkhat.in.peers.connection.FileQueue;
import sakkhat.in.peers.connection.FileReceiver;
import sakkhat.in.peers.connection.FileSender;
import sakkhat.in.peers.connection.Engine;
import sakkhat.in.peers.connection.Router;
import sakkhat.in.peers.generic.FileUtil;
import sakkhat.in.peers.model.FileIO;

public class FileShare extends Template
    implements Handler.Callback{

    private static final int FILE_CHOOSE_REQUEST = 5;
    private static final int FOLDER_SELECTOR = 6;


    private Button pickFile, storePath;
    private ListView fileIOListView;

    private Handler handler;
    private Router.Receiver routerReceiver;
    private FileSender fileSender;
    private volatile FileQueue sendingQueue;

    private ArrayList<FileIO> fileIOList;
    private int receivingIndex, sendingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTemplate(R.layout.activity_file_share);

        handler = new Handler(this);
        sendingQueue = FileQueue.init();
        initRouter();
    }

    @Override
    public void initLayout(){
        pickFile = (Button) findViewById(R.id.pickFile);
        storePath = (Button) findViewById(R.id.storePath);
        fileIOListView = (ListView) findViewById(R.id.fileIOListView);
    }

    @Override
    public void initListeners(){
        pickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, FILE_CHOOSE_REQUEST);

            }
        });

        storePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FileShare.this, FolderPicker.class);
                startActivityForResult(i, FOLDER_SELECTOR);

            }
        });
    }

    private void initRouter(){
        routerReceiver = Router.Receiver.init(handler);
        fileIOList = new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;

        if(requestCode == FILE_CHOOSE_REQUEST && data.getData()!= null){
            String path = FileUtil.getPath(FileShare.this, data.getData());
            if(path != null){
                File selectedFile = new File(path);
                sendingQueue.enqueue(selectedFile);
                fileSender = FileSender.init(handler, sendingQueue);
                sendingIndex = fileIOList.size();
                fileIOList.add(FileIO.init(selectedFile.getName(), FileIO.SENDING));
                // adapter
                Toast.makeText(this, "selected: "+selectedFile.getName(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == FOLDER_SELECTOR){
            FileReceiver.PATH= data.getExtras().getString("data");
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case Engine.FILE_RECEIVE_REQUEST:
                String name = (String) msg.obj;
                receivingIndex = fileIOList.size();
                fileIOList.add(FileIO.init(name, FileIO.RECEIVING));
                // adapter
                return true;

            case Engine.FILE_RECEIVED:
                fileIOList.get(receivingIndex).setStreaming(false);
                // adapter update
                return true;

            case Engine.FILE_SENT:
                fileIOList.get(sendingIndex).setStreaming(false);
                // adapter
                return true;
        }
        return false;
    }
}
