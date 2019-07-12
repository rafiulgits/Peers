package sakkhat.in.peers.adapter;


import android.content.Context;

import java.util.List;

/**
 * Created by Rafiul Islam on 12-Jul-19.
 */

public class AdapterFactory {
    public static final int DEVICE_LIST_ADAPTER = 1;
    public static final int FILE_IO_LIST_ADAPTER = 2;
    public static Adapter get(int type, Context context, int layoutId, List list){
        if(type == DEVICE_LIST_ADAPTER){
            return new DeviceListAdapter(context, layoutId, list);
        }
        else if(type == FILE_IO_LIST_ADAPTER){
            return new FileIOListAdapter(context, layoutId, list);
        }
        return null;
    }
}
