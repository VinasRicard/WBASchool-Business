package com.example.android.communication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.Notifications.Token;

import com.example.android.communication.R;
import com.example.android.communication.Classes.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String accountType;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavegationView;
    private TextView usernameTv, centerTv1, centerTv2;
    private LinearLayout usernameChat, anonymousChat;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views
        usernameTv = findViewById(R.id.maUsernameTv);
        centerTv1 = findViewById(R.id.maCenterTv1);
        centerTv2 = findViewById(R.id.maCenterTv2);
        mDrawerLayout = findViewById(R.id.drawer);
        usernameChat = findViewById(R.id.usernameChat);
        anonymousChat = findViewById(R.id.anonymousChat);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Check if the user is null
        if (firebaseUser != null) {
            //Check if the user is a school/company and if it is, go to Main Activity 2
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile user = dataSnapshot.getValue(UserProfile.class);
                    if (user.getAccountType().equals("company") | user.getAccountType().equals("school")) {
                        Intent goToMain2 = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(goToMain2);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    } else {
                        usernameTv.setText(user.getUserName());
                        centerTv1.setText(user.getUserCenter());
                        centerTv2.setText(user.getUserCenter());
                        accountType = user.getAccountType();
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        findViewById(R.id.white).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            //Go to First Activity
            Intent intent = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }


        //Set up the drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavegationView = findViewById(R.id.navegationView);
        mNavegationView.setNavigationItemSelectedListener(this);

        //App theme
        SharedPreferences prefs = getSharedPreferences("APP_THEME", MODE_PRIVATE);
        String theme = prefs.getString("theme", "light");
        if (theme != null) {
            if (theme.equals("light")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (theme.equals("dark")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }


        //Set up the Buttons
        usernameChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToUsernameChat = new Intent(MainActivity.this, ChatActivity.class);
                goToUsernameChat.putExtra("chatType", "username");
                startActivity(goToUsernameChat);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        anonymousChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAnonymousChat = new Intent(MainActivity.this, ChatActivity.class);
                goToAnonymousChat.putExtra("chatType", "anonymous");
                startActivity(goToAnonymousChat);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


        //Language
        language = Locale.getDefault().getDisplayLanguage();


    }

    //Left Drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //When and item of the drawer is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profileOption:
                //Go to profile activity
                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.settingsOption:
                //Go to profile activity
                Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(goToSettings);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.informationOption:
                //Go to the info about bullying and mobbing
                if (language.equals("english")) {
                    if (accountType.equals("student")) {
                        String url = "https://drive.google.com/open?id=13cQSM51t0l5GsZGEDApiOonceMmO8VFaI_w_scdwA2s";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (accountType.equals("worker")) {
                        String url = "https://docs.google.com/document/d/1ek8_vqiLJSjlATBYXkGkBv4dmppGTvaNAEPY15LM_o8/edit?usp=sharing";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else if (language.equals("español")) {
                    if (accountType.equals("student")) {
                        String url = "https://drive.google.com/open?id=1Nw1GTwAQ2bSRtM4I3jmX9FF_Ja0X0rnOfuLLU6Lrnss";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (accountType.equals("worker")) {
                        String url = "https://drive.google.com/open?id=1CDSbtJd8COOzGRlmqsS_BHIajfXmGbTaonth5c42KMI";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else if (language.equals("català")) {
                    if (accountType.equals("student")) {
                        String url = "https://drive.google.com/open?id=1GqDbcRWcH9Xp62Lkm0l7KsrDv4ixQDPhfixGWMU4xkg";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (accountType.equals("worker")) {
                        String url = "https://drive.google.com/open?id=1u7n3i2PdJ7WP__AjwraWQK_U9nBzODfk62BsrsOTn_U";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
                break;
            case R.id.legalOption:
                //Go to the info about bullying and mobbing
                if (language.equals("english")) {
                    String url = "https://docs.google.com/document/d/1mBcf4rYC9T01Wq_a_GMH1R8ziRwhg8pTzDXlEukEELE/edit?usp=sharing";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if (language.equals("español")) {
                    String url = "https://docs.google.com/document/d/1a3cvMo2HxayGcMz_x07-RFeY87IzeF6Qce1c9LfypO0/edit?usp=sharing";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if (language.equals("català")) {
                    String url = "https://docs.google.com/document/d/1Qv22n6bGgJ9KDKRPMp2igQtQvFHjl8mixOIbpfR7wa0/edit?usp=sharing";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                break;
            case R.id.helpOption:
                //Send mail to support
                Intent findHelp = new Intent(Intent.ACTION_SENDTO);
                findHelp.setData(Uri.parse("mailto: support@wba.school&business.com")); // only email apps should handle this
                findHelp.putExtra(findHelp.EXTRA_SUBJECT, "Help");
                startActivity(findHelp);
                break;
            case R.id.logoutOption:
                //Log out and go to First Activity
                Toast.makeText(this, getString(R.string.logoutOption), Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent logOut = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(logOut);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
        }
        return true;
    }

    //Device BackBtn
    @Override
    public void onBackPressed() {

    }
}
