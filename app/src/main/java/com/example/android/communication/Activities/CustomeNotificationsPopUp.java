package com.example.android.communication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.R;

import org.w3c.dom.Text;

public class CustomeNotificationsPopUp extends AppCompatActivity {

    RadioGroup radioGroup;
    TextView okay;
    RadioButton radio1, radio2, radio3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_notifications_pop_up);

        //Pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .3));
        getWindow().setElevation(10);

        //Views
        radioGroup = findViewById(R.id.radioGroup);
        okay = findViewById(R.id.okayTv);
        radio1 = findViewById(R.id.button1);
        radio2 = findViewById(R.id.button2);
        radio3 = findViewById(R.id.button3);


        //Check notifications
        SharedPreferences preferences = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE);
        String notifications = preferences.getString("notifications", "activated");
        if (notifications != null) {
            if (notifications.equals("activated")) {
                radio1.setChecked(true);
            } else if (notifications.equals("activated2")) {
                radio2.setChecked(true);
            } else if (notifications.equals("activated3")) {
                radio3.setChecked(true);
            }
        }

        //Save btn
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio1.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE).edit();
                    editor.putString("notifications", "activated");
                    editor.apply();
                } else if (radio2.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE).edit();
                    editor.putString("notifications", "activated2");
                    editor.commit();
                } else if (radio3.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE).edit();
                    editor.putString("notifications", "activated3");
                    editor.commit();
                }

                finish();
            }
        });

    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        finish();
    }
}
