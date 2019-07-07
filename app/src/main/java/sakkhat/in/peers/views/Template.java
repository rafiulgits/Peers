package sakkhat.in.peers.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class Template extends AppCompatActivity {

    public abstract void initLayout();
    public abstract void initListeners();

    public void loadTemplate(int viewId){
        setContentView(viewId);
        initLayout();
        initListeners();
    }
}

