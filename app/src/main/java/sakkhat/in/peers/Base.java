package sakkhat.in.peers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import sakkhat.in.peers.adapter.DeviceListAdapter;
import sakkhat.in.peers.broadcaster.WiFiStateReceiver;
import sakkhat.in.peers.connection.Listener;
import sakkhat.in.peers.connection.ConnectionManager;
import sakkhat.in.peers.connection.P2P;
import sakkhat.in.peers.generic.Permission;
import sakkhat.in.peers.pages.Index;

public class Base
        extends AppCompatActivity
        implements
            Handler.Callback,
            WifiP2pManager.PeerListListener,
            WifiP2pManager.ConnectionInfoListener,
            WifiP2pManager.ActionListener   {

    public static final String TAG = "base_activity";

    private static final int PERMISSION_FILE_READ = 1001;
    private static final int PERMISSION_FILE_WRITE = 1002;
    private static final int PERMISSION_ACCESS_WIFI_STATE = 1003;
    private static final int PERMISSION_CHANGE_WIFI_STATE = 1004;
    private static final int PERMISSION_INTERNET = 1005;
    private static final int PERMISSION_ACCESS_NETWORK_STATE = 1006;
    private static final int PERMISSOIN_CHANGE_NETWORK_STATE = 1007;

    private TextView baseText;
    private ListView deviceListView;
    private Button searchButton;
    private ProgressBar searchProgress;

    private IntentFilter intentFilter;
    private WiFiStateReceiver wiFiStateReceiver;

    private WifiP2pManager p2pManager;
    private WifiP2pManager.Channel p2pChannel;

    private List<WifiP2pDevice> discoverPeers;
    private List<String> deviceNameList;
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initLayout();
        initConnectionInterface();
        initListener();
        permissionChecker();
    }

    private void initLayout(){
        baseText = (TextView) findViewById(R.id.baseText);
        deviceListView = (ListView) findViewById(R.id.baseDeviceListView);
        searchButton = (Button) findViewById(R.id.baseSearchButton);
        searchProgress = (ProgressBar) findViewById(R.id.baseSearchingProgress);
    }

    private void initListener(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p2pManager == null) return;
                p2pManager.discoverPeers(p2pChannel, Base.this);
                searchProgress.setVisibility(View.VISIBLE);
            }
        });

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = discoverPeers.get(position).deviceAddress;
                p2pManager.connect(p2pChannel,config, Base.this);
                Toast.makeText(Base.this, "Connection establishing...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initConnectionInterface(){
        p2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        p2pChannel = p2pManager.initialize(this, getMainLooper(), null);


        if(p2pManager == null || p2pChannel == null){
            return;
        }

        ConnectionManager.setP2pManager(p2pManager);
        ConnectionManager.setP2pChannel(p2pChannel);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wiFiStateReceiver = new WiFiStateReceiver(p2pManager, p2pChannel, TAG);
        wiFiStateReceiver.setListeners(this, this);

        discoverPeers = new ArrayList<>();
        deviceNameList = new ArrayList<String>();

        deviceListAdapter = new DeviceListAdapter(this, R.layout.item_device_list, deviceNameList);
        deviceListView.setAdapter(deviceListAdapter);
    }


    private void permissionChecker(){
        if(!Permission.has(this, Permission.READ_STORAGE)){
            Permission.request(this, Permission.READ_STORAGE, PERMISSION_FILE_READ);
        }
        if(!Permission.has(this, Permission.WRITE_STORAGE)){
            Permission.request(this, Permission.WRITE_STORAGE, PERMISSION_FILE_WRITE);
        }
        if(!Permission.has(this, Permission.ACCESS_WIFI_STATE)){
            Permission.request(this, Permission.ACCESS_WIFI_STATE, PERMISSION_ACCESS_WIFI_STATE);
        }
        if(!Permission.has(this, Permission.CHANGE_WIFI_STATE)){
            Permission.request(this, Permission.CHANGE_WIFI_STATE, PERMISSION_CHANGE_WIFI_STATE);
        }
        if(!Permission.has(this, Permission.INTERNET)){
            Permission.request(this, Permission.INTERNET, PERMISSION_INTERNET);
        }
        if(!Permission.has(this, Permission.ACCESS_NETWORK_STATE)){
            Permission.request(this, Permission.ACCESS_NETWORK_STATE, PERMISSION_ACCESS_NETWORK_STATE);
        }
        if(!Permission.has(this, Permission.CHANEGE_NETWORK_STATE)){
            Permission.request(this, Permission.CHANEGE_NETWORK_STATE, PERMISSOIN_CHANGE_NETWORK_STATE);
        }


    }

    private void initSocketInterface(InetAddress address, boolean isOwner){
        if(isOwner){
            P2P.Server server = P2P.Server.init(new Handler(this));
            server.execute();
        }
        else{
            P2P.Client client = P2P.Client.init(address, new Handler(this));
            client.execute();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_FILE_READ){
            if(!Permission.isGranted(grantResults)){
                Permission.request(this,Permission.READ_STORAGE, PERMISSION_FILE_READ);
            }
        }

        if(requestCode == PERMISSION_FILE_WRITE){
            if(!Permission.isGranted(grantResults)){
                Permission.request(this,Permission.WRITE_STORAGE, PERMISSION_FILE_WRITE);
            }
        }
        if(requestCode == PERMISSION_ACCESS_WIFI_STATE){
            if(Permission.isGranted(grantResults)){
                Permission.request(this,Permission.ACCESS_WIFI_STATE, PERMISSION_ACCESS_WIFI_STATE);
            }
        }
        if(requestCode == PERMISSION_CHANGE_WIFI_STATE) {
            if(! Permission.isGranted(grantResults)) {
                Permission.request(this,Permission.CHANGE_WIFI_STATE, PERMISSION_CHANGE_WIFI_STATE);
            }

        }
        if(requestCode == PERMISSION_INTERNET) {
            if(!Permission.isGranted(grantResults)) {
                Permission.request(this,Permission.CHANGE_WIFI_STATE, PERMISSION_INTERNET);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wiFiStateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wiFiStateReceiver, intentFilter);
    }

    @Override
    public void onSuccess() {
        Log.d(TAG, "request successfully posted");
    }

    @Override
    public void onFailure(int reason) {
        switch (reason){
            case WifiP2pManager.BUSY:{
            } break;

            case WifiP2pManager.ERROR:{
            } break;

            case WifiP2pManager.P2P_UNSUPPORTED: {
            } break;

            case WifiP2pManager.NO_SERVICE_REQUESTS: {
            } break;
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if(info.isGroupOwner && info.groupFormed){
            initSocketInterface(info.groupOwnerAddress, true);
        }
        else if (info.groupFormed) {
            initSocketInterface(info.groupOwnerAddress, false);
        }

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        searchProgress.setVisibility(View.GONE);
        if(discoverPeers.isEmpty() || !discoverPeers.equals(peers)){

            discoverPeers.clear();
            deviceNameList.clear();
            deviceListAdapter.notifyDataSetChanged();

            discoverPeers.addAll(peers.getDeviceList());
            for(WifiP2pDevice device : discoverPeers){
                deviceNameList.add(device.deviceName);
            }

            deviceListAdapter.notifyDataSetChanged();

            Log.d(TAG, "peer list collected");
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case Listener.SOCKET_ERROR:
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                return true;

            case Listener.SOCKET_ESTABLISHED:
                this.finish();
                startActivity(new Intent(this, Index.class));
                return true;
        }
        return false;
    }


}
