package com.example.android.communication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.communication.Fragments.AllMessagesFragment;
import com.example.android.communication.Fragments.ArchivedFragment;
import com.example.android.communication.Fragments.ImportantsFragment;
import com.example.android.communication.R;
import com.example.android.communication.Classes.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String accountType;
    private ActionBar actionbar;
    private DrawerLayout mDrawerLayout2;
    private ActionBarDrawerToggle mToggle2;
    private NavigationView mNavegationView2;
    private TabItem tabItem1, tabItem2, tabItem3;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Action Bar
        actionbar = getSupportActionBar();

        //TabLayout
        TabLayout tablayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.container);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ArchivedFragment(), getResources().getString(R.string.archived));
        viewPagerAdapter.addFragment(new AllMessagesFragment(), getResources().getString(R.string.allMessages));
        viewPagerAdapter.addFragment(new ImportantsFragment(), getResources().getString(R.string.importants));
        viewPager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);


        //Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                //Put the name of the school/company in the Action Bar
                actionbar.setTitle(user.getUserName());
                accountType = (user.getAccountType());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Set up the drawer
        mDrawerLayout2 = findViewById(R.id.drawer2);
        mToggle2 = new ActionBarDrawerToggle(this, mDrawerLayout2, R.string.open, R.string.close);
        mDrawerLayout2.addDrawerListener(mToggle2);
        mToggle2.syncState();
        actionbar.setDisplayHomeAsUpEnabled(true);
        mNavegationView2 = findViewById(R.id.navegationView2);
        mNavegationView2.setNavigationItemSelectedListener(this);

        //Default anguage
        language = "english";

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
    }

    //Left Drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle2.onOptionsItemSelected(item)) {
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
                Intent goToProfile = new Intent(MainActivity2.this, ProfileActivity2.class);
                startActivity(goToProfile);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.settingsOption:
                //Go to profile activity
                Intent goToSettings = new Intent(MainActivity2.this, SettingsActivity.class);
                startActivity(goToSettings);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.informationOption:
                //Go to the info about bullying and mobbing
                Toast.makeText(this, "Bullying and Mobbing", Toast.LENGTH_SHORT).show();
                if (language.equals("english")) {
                    if (accountType.equals("school")) {
                        String url = "https://docs.google.com/document/d/13yNhUYmW-vMqR0pl3F-r3iLlTpzO6dV1D4x-Zt_V9FI/edit?usp=sharing";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (accountType.equals("company")) {
                        String url = "https://drive.google.com/open?id=1RNx-f4owfwpfS30TPtMdXG2gfQ080VYx4qGXJBztgdM";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else if (language.equals("spanish")) {
                    if (accountType.equals("school")) {
                        String url = "https://drive.google.com/open?id=16jSttGd5S8ygwGRZQQJ1pUwjyUHdPne3RbTBp4nlFwk";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (accountType.equals("company")) {
                        String url = "https://drive.google.com/open?id=1tkyFTppdNAmQCKIliEg4n3BB3G4B-BxI6GnpXaw701E";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else if (language.equals("catalan")) {
                    if (accountType.equals("school")) {
                        String url = "https://drive.google.com/open?id=1jv0TtrBT4wmVWBlPP0dyYxVsgPO5vply65_5lwEWeY0";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (accountType.equals("company")) {
                        String url = "https://drive.google.com/open?id=1I2Nl_bKSyMQMfl97su3W1o1kOgt9HdKLF3TCMZH1jwY";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
                break;
            case R.id.foldersOption:
                Intent goToFolders = new Intent(MainActivity2.this, FoldersActivity.class);
                startActivity(goToFolders);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.helpOption:
                //Send mail to support
                Intent findHelp = new Intent(Intent.ACTION_SENDTO);
                findHelp.setData(Uri.parse("mailto: support@wba.school&business.com")); // only email apps should handle this
                findHelp.putExtra(findHelp.EXTRA_SUBJECT, "WBA School");
                startActivity(findHelp);
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
            case R.id.logoutOption:
                //Log out and go to First Activity
                Toast.makeText(this, getString(R.string.logoutOption), Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent logOut = new Intent(MainActivity2.this, FirstActivity.class);
                startActivity(logOut);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
        }
        return true;
    }

    //Fragments
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
