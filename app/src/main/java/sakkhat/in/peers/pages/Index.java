package sakkhat.in.peers.pages;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sakkhat.in.peers.Base;
import sakkhat.in.peers.R;
import sakkhat.in.peers.connection.ConnectionManager;
import sakkhat.in.peers.connection.Listener;
import sakkhat.in.peers.connection.Router;


public class Index extends AppCompatActivity
        implements Handler.Callback,
        WifiP2pManager.ActionListener {

    private static final String TAG = "index_page";
    public static final int GOTO_FILE_SHARING = 41;
    public static final int GOTO_CONTROL_SHARING = 42;

    private CardView goFileSharing, goControlSharing;
    private Button disconnect;
    private Router.Sender sender;
    private Router.Receiver receiver;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        goFileSharing = (CardView) findViewById(R.id.indexGoFileShare);
        goControlSharing = (CardView) findViewById(R.id.indexGoControlShare);
        disconnect = (Button) findViewById(R.id.indexDisconnectButton);

        handler = new Handler(this);

        listenSocket();

        goFileSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender = Router.Sender.init(handler, Index.GOTO_FILE_SHARING);
                sender.execute();
                fileSharingPage();
            }
        });

        goControlSharing.setOnClickListener(new View.OnClickListener() {
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

    private void fileSharingPage(){
        receiver.terminate();
        this.finish();
        startActivity(new Intent(this, FileSharing.class));
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case Listener.SOCKET_ERROR:
                goToBase();
                return true;

            case Index.GOTO_FILE_SHARING:
                fileSharingPage();
                return true;


            case Index.GOTO_CONTROL_SHARING:
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
