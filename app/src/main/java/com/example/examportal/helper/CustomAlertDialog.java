package com.example.examportal.helper;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.examportal.R;
import com.example.examportal.SplashScreenActivity;
import com.example.examportal.WelcomeActivity;

public class CustomAlertDialog {

    private AlertDialog dialog;

    public CustomAlertDialog(Context context,String message,String positive,String negative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.sample_alert, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        TextView Message = dialogView.findViewById(R.id.message);
        Button ok=dialogView.findViewById(R.id.ok);
        Button check=dialogView.findViewById(R.id.aCheck);

        if (message!=null || positive!=null||negative!=null)
        {
            Message.setText(message);
            ok.setText(positive);
            check.setText(negative);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    ((Activity) context).finish();
                }
            });
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }
        else
        {
            Message.setText("No Network Available !");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                    ((Activity) context).finish();

                }
            });

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    context.startActivity(intent);
                    dismiss();
                }
            });
        }


        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();

    }
}
