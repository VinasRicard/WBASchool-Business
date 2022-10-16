package com.example.android.communication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.R;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private ImageView backBtn;
    private Switch notificationsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Views
        backBtn = findViewById(R.id.backBtnSettings);
        Spinner spinner2 = (Spinner) findViewById(R.id.themeSp);
        final TextView notificationsSwitchTxt = findViewById(R.id.notificationsSwitchTxt);
        final TextView customeNotifications = findViewById(R.id.customeNotifications);
        Button shareAppBtn = (Button) findViewById(R.id.shareAppBtn);
        Button rateAppBtn = (Button) findViewById(R.id.rateAppBtn);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);

        //Check notifications
        SharedPreferences preferences = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE);
        String notifications = preferences.getString("notifications", "activated");
        if (notifications != null) {
            if (notifications.equals("disabled")) {
                notificationsSwitch.setChecked(false);
                notificationsSwitchTxt.setText(getString(R.string.desactivated));
                customeNotifications.setVisibility(View.INVISIBLE);
            } else if (notifications.equals("activated") || notifications.equals("activated2") || notifications.equals("activated3")) {
                notificationsSwitch.setChecked(true);
                notificationsSwitchTxt.setText(getString(R.string.activated));
                customeNotifications.setVisibility(View.VISIBLE);
            }
        }

        //ActionBar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.settingsOption);
        actionbar.setDisplayHomeAsUpEnabled(false);

        //Back Btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(backToMain);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //Spinner of themes
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.themes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        //Notifications switch
        notificationsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationsSwitch.isChecked()) {
                    notificationsSwitchTxt.setText(getString(R.string.activated));
                    SharedPreferences.Editor editor = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE).edit();
                    editor.putString("notifications", "activated");
                    editor.apply();
                    customeNotifications.setVisibility(View.VISIBLE);
                } else if (!notificationsSwitch.isChecked()) {
                    notificationsSwitchTxt.setText(getString(R.string.desactivated));
                    SharedPreferences.Editor editor = getSharedPreferences("NOTIFICATIONS", MODE_PRIVATE).edit();
                    editor.putString("notifications", "disabled");
                    editor.commit();
                    customeNotifications.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Custome notifications
        customeNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customeNotifications = new Intent(SettingsActivity.this, CustomeNotificationsPopUp.class);
                startActivity(customeNotifications);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //Share App Btn
        shareAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Share the App", Toast.LENGTH_SHORT).show();
            }
        });

        //Rate App Btn
        rateAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Rate the App", Toast.LENGTH_SHORT).show();
            }
        });

        //Check theme
        SharedPreferences prefs = getSharedPreferences("APP_THEME", MODE_PRIVATE);
        String theme = prefs.getString("theme", "light");
        if (theme != null) {
            if (theme.equals("light")) {
                spinner2.setSelection(0);
            } else if (theme.equals("dark")) {
                spinner2.setSelection(1);
            }
        }

    }

    //Theme selector ClickListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemText = (String) parent.getItemAtPosition(position);
        if (selectedItemText.equals("Light") || selectedItemText.equals("Claro") || selectedItemText.equals("Clar")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferences.Editor editor = getSharedPreferences("APP_THEME", MODE_PRIVATE).edit();
            editor.putString("theme", "light");
            editor.commit();
        } else if (selectedItemText.equals("Dark") || selectedItemText.equals("Oscuro") || selectedItemText.equals("Fosc")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferences.Editor editor = getSharedPreferences("APP_THEME", MODE_PRIVATE).edit();
            editor.putString("theme", "dark");
            editor.commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Device Back Btn
    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(backToMain);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
