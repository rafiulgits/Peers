package sakkhat.in.peers.pages;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sakkhat.in.peers.Base;
import sakkhat.in.peers.R;
import sakkhat.in.peers.connection.ConnectionManager;

public class Index extends AppCompatActivity {

    private CardView goFileSharing, goControlSharing;
    private Button disconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        goFileSharing = (CardView) findViewById(R.id.indexGoFileShare);
        goControlSharing = (CardView) findViewById(R.id.indexGoControlShare);
        disconnect = (Button) findViewById(R.id.indexDisconnectButton);

        goFileSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Index.this, FileSharing.class));
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
                WifiP2pManager p2pManager = ConnectionManager.getP2pManager();
                WifiP2pManager.Channel p2pChannel = ConnectionManager.getP2pChannel();

                p2pManager.removeGroup(p2pChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        goToBase();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(Index.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void goToBase(){
        this.finish();
        startActivity(new Intent(this, Base.class));
    }
}
