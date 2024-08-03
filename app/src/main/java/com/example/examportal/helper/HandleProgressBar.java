package com.example.examportal.helper;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ProgressBar;

public class HandleProgressBar {

    ProgressBar id;
    public HandleProgressBar(ProgressBar id){
        this.id=id;
    }

    public void show()
    {
        id.setVisibility(View.VISIBLE);
        id.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) id.getIndeterminateDrawable()).start();
            }
        });
    }

    public void dismiss()
    {
        ((AnimationDrawable) id.getIndeterminateDrawable()).stop();
        id.setVisibility(View.GONE);
    }
}
