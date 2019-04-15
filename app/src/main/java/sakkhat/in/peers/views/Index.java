package sakkhat.in.peers.views;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import lib.folderpicker.FolderPicker;
import sakkhat.in.peers.R;
import sakkhat.in.peers.connection.ConnectionManager;
import sakkhat.in.peers.connection.FileQueue;
import sakkhat.in.peers.connection.FileReceiver;
import sakkhat.in.peers.connection.Listener;
import sakkhat.in.peers.connection.Router;
import sakkhat.in.peers.generic.FileUtil;


public class Index extends AppCompatActivity
        implements Handler.Callback,
        WifiP2pManager.ActionListener {

    private static final String TAG = "index_page";

    private static final int FOLDER_SELECT = 1;
    private static final int FILE_SELECT = 2;
    private static final int CAMERA_SELECT = 3;

    private FloatingActionButton pickFile, selectStograge, ioProgress, cameraFrame;

    private Button disconnect;
    private Router.Sender sender;
    private Router.Receiver receiver;
    private Handler handler;
    private volatile FileQueue sendingQueue;

    private BottomSheetBehavior bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initLayout();
        handler = new Handler(this);
        sendingQueue = FileQueue.init();
        clickEvents();
        listenSocket();
    }

    private void initLayout(){
        pickFile = (FloatingActionButton) findViewById(R.id.pickFile);
        selectStograge = (FloatingActionButton) findViewById(R.id.storage);
        ioProgress = (FloatingActionButton) findViewById(R.id.ioProgress);
        cameraFrame = (FloatingActionButton) findViewById(R.id.cameraShare);
        disconnect = (Button) findViewById(R.id.indexDisconnectButton);
    }

    private void clickEvents(){
        pickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, FILE_SELECT);

            }
        });

        selectStograge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Index.this, FolderPicker.class);
                startActivityForResult(i, FOLDER_SELECT);
            }
        });

        ioProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // progress dialog bar
            }
        });

        cameraFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBase();
            }
        });
    }

    private void goToBase(){
        receiver.terminate();
        ConnectionManager.disconnect(this);
        this.finish();
        startActivity(new Intent(this, Base.class));
    }

    private void listenSocket(){
        receiver = Router.Receiver.init(handler);
        receiver.execute();
    }

    private void initBottomSheet(){
        View view = findViewById(R.id.cameraShare);
        bottomSheet = BottomSheetBehavior.from(view);
        bottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case  BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if(requestCode == FILE_SELECT && data.getData()!= null){
            String path = FileUtil.getPath(this, data.getData());
            if(path != null){
                File selectedFile = new File(path);
                sendingQueue.enqueue(selectedFile);
                Toast.makeText(this, "selected: "+selectedFile.getName(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == FOLDER_SELECT){
            FileReceiver.PATH= data.getExtras().getString("data");
            Toast.makeText(this, "Storage folder updated", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case Listener.SOCKET_ERROR:
                goToBase();
                return true;
        }
        return false;
    }

    @Override
    public void onSuccess() {
        Log.d(TAG, "disconnected");
    }

    @Override
    public void onFailure(int i) {
        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "fail to disconnect the p2p manager");
    }
}
