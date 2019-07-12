package sakkhat.in.peers.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Rafiul Islam on 12-Jul-19.
 */

public abstract class Adapter<T> extends ArrayAdapter<T>{

    public Adapter(Context context, int resource, List list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
