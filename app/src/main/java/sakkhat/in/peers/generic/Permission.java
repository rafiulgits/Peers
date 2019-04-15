package sakkhat.in.peers.generic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Rafiul Islam on 13-Apr-19.
 */

public class Permission {
    public static final int INTERNET = 1;
    public static final int READ_STORAGE = 2;
    public static final int WRITE_STORAGE = 3;
    public static final int ACCESS_WIFI_STATE = 5;
    public static final int CHANGE_WIFI_STATE = 6;
    public static final int RECORD_AUDIO = 7;
    public static final int ACCESS_NETWORK_STATE = 8;
    public static final int CHANEGE_NETWORK_STATE = 9;

    public static boolean has(Context context, int label){
        switch (label){
            case INTERNET:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;

            case READ_STORAGE:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;

            case WRITE_STORAGE:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;


            case ACCESS_WIFI_STATE:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;

            case CHANGE_WIFI_STATE:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;

            case RECORD_AUDIO:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;


            case ACCESS_NETWORK_STATE:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;


            case CHANEGE_NETWORK_STATE:
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_NETWORK_STATE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return false;
                } return true;

        }

        return false;
    }

    public static void request(Activity activity, int label, int requestCode){
        switch (label){
            case INTERNET:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.INTERNET},requestCode);
                break;

            case READ_STORAGE:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
                break;

            case WRITE_STORAGE:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode);
                break;

            case ACCESS_WIFI_STATE:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_WIFI_STATE},requestCode);
                break;

            case CHANGE_WIFI_STATE:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CHANGE_WIFI_STATE},requestCode);
                break;

            case RECORD_AUDIO:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECORD_AUDIO},requestCode);
                break;

            case ACCESS_NETWORK_STATE:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},requestCode);
                break;

            case CHANEGE_NETWORK_STATE:
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CHANGE_NETWORK_STATE},requestCode);
                break;


        }

    }

    public static boolean isGranted(int[] grantResults){
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

}
