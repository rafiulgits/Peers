package sakkhat.in.peers.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sakkhat.in.peers.R;
import sakkhat.in.peers.model.FileIO;

/**
 * Created by Rafiul Islam on 17-Apr-19.
 */

public class FileIOListAdapter extends ArrayAdapter<FileIO> {

    private Context context;
    private int layout;
    private ArrayList<FileIO> list;

    public FileIOListAdapter(Context context, ArrayList<FileIO> list){
        super(context, R.layout.item_sharing, list);
        this.context = context;
        this.list = list;
        layout = R.layout.item_sharing;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FileIOListAdapter.Holder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null, false);

            holder = new FileIOListAdapter.Holder(row);
            row.setTag(holder);
        }
        else{
            holder = (FileIOListAdapter.Holder) row.getTag();
            FileIO obj = list.get(position);
            holder.fileName.setText(obj.getName());
            if(obj.isStreaming()){
                if(obj.getType() == obj.SENDING){
                    holder.fileStatus.setText("sending");
                }
                else{
                    holder.fileStatus.setText("receiving");
                }
            }
            else{
                if(obj.getType() == obj.SENDING){
                    holder.fileStatus.setText("sent");
                }
                else{
                    holder.fileStatus.setText("received");
                }
            }
        }

        return row;
    }

    private static class Holder {
        TextView fileName;
        TextView fileStatus;

        public Holder(View view){
            fileName = view.findViewById(R.id.fileName);
            fileStatus = view.findViewById(R.id.fileStatus);
        }
    }
}
