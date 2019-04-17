package sakkhat.in.peers.views;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sakkhat.in.peers.R;
import sakkhat.in.peers.connection.ConnectionManager;

public class Index extends AppCompatActivity
        implements WifiP2pManager.ActionListener{

    private Button disconnect, fileShare, cameraShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initLayout();
        initClickEvents();
    }

    private void initLayout(){
        disconnect = (Button) findViewById(R.id.disconnect);
        fileShare = (Button) findViewById(R.id.gotoFileShare);
        cameraShare = (Button) findViewById(R.id.gotoShareCamera);
    }

    private void initClickEvents(){
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFileShare();
            }
        });

        cameraShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCameraShare();
            }
        });
    }

    private void gotoBase(){
        ConnectionManager.disconnect(this);
        this.finish();
        startActivity(new Intent(this, Base.class));
    }
    private void gotoFileShare(){
        this.finish();
        startActivity(new Intent(this, FileShare.class));
    }

    private void gotoCameraShare(){

    }


    @Override
    public void onSuccess() {
        gotoBase();
    }

    @Override
    public void onFailure(int reason) {
        ConnectionManager.disconnect(this);
        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
    }
}
