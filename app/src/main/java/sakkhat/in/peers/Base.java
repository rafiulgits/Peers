package sakkhat.in.peers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sakkhat.in.peers.generic.Permission;

public class Base extends AppCompatActivity {

    private static final int PERMISSION_FILE_READ = 1001;
    private static final int PERMISSION_FILE_WRITE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // TODO:
        // init interface
        // permission checker
        // init listeners
    }

    private void permissionChecker(){
        if(!Permission.has(this, Permission.READ_STORAGE)){
            Permission.request(this, Permission.READ_STORAGE, PERMISSION_FILE_READ);
        }
        if(!Permission.has(this, Permission.WRITE_STORAGE)){
            Permission.request(this, Permission.WRITE_STORAGE, PERMISSION_FILE_WRITE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_FILE_READ){
            if(Permission.isGranted(grantResults)){

            }
            else{
                Permission.request(this,Permission.ACCESS_WIFI_STATE, PERMISSION_FILE_READ);
            }
        }

        if(requestCode == PERMISSION_FILE_WRITE){
            if(Permission.isGranted(grantResults)){

            }
            else{
                Permission.request(this,Permission.ACCESS_WIFI_STATE, PERMISSION_FILE_WRITE);
            }
        }
    }
}
