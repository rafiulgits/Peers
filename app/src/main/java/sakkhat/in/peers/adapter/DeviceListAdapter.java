package sakkhat.in.peers.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakkhat.in.peers.R;

public class DeviceListAdapter extends Adapter<String> {

    private Context context;
    private int layoutID;
    private List<String> devices;

    public DeviceListAdapter(Context context, int layoutID,  List<String> devices) {
        super(context, layoutID, devices);

        this.context = context;
        this.layoutID = layoutID;
        this.devices = devices;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutID, null, false);

            holder = new Holder(row);
            row.setTag(holder);
        }
        else{
            holder = (Holder) row.getTag();
            holder.textView.setText(devices.get(position));
        }

        return row;
    }

    private static class Holder {
        TextView textView;

        public Holder(View view){
            textView = view.findViewById(R.id.deviceItemName);
        }
    }
}
