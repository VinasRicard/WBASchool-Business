package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.android.communication.Adapters.MyRecyclerViewAdapter;
import com.example.android.communication.R;
import com.example.android.communication.Classes.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateAccountActivity2 extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {


    private ImageView backBtn;
    private SearchView searchEt;
    private String accountType2;
    private String center;
    private ArrayList<String> schoolNames;
    private RecyclerView schoolsList;
    MyRecyclerViewAdapter adapter;
    DatabaseReference reference;
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account2);

        //Views
        searchEt = findViewById(R.id.searchEt);
        schoolsList = findViewById(R.id.schoolsList);
        backBtn = findViewById(R.id.backBtn);


        //Get user accountType
        Intent intent = getIntent();
        accountType2 = intent.getStringExtra("accountType");
        //If the user is a school or a company, skip the activity
        if (accountType2.equals("school") | accountType2.equals("company")) {
            Intent temporal = new Intent(CreateAccountActivity2.this, SchoolorCompanyLicence.class);
            //Send user account
            temporal.putExtra("accountType2", accountType2);
            startActivity(temporal);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            center = "null";
        }

        //Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //List
        //Get the school/company names and display them in a Recycler Viewer
        schoolNames = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //If the user is a worker, change the hint of the Search Bar Edit Text
        if (accountType2.equals("worker")) {
            //Search bar
            searchEt.setQueryHint((getString(R.string.searchCompany)));
            //Add the companies to the Array List
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    assert userProfile != null;
                    if (userProfile.getAccountType().equals("company")) {
                        schoolNames.add(userProfile.getUserName());
                        // set up the RecyclerView
                        schoolsList.setLayoutManager(new LinearLayoutManager(CreateAccountActivity2.this));
                        adapter = new MyRecyclerViewAdapter(CreateAccountActivity2.this, schoolNames);
                        adapter.setClickListener(CreateAccountActivity2.this);
                        schoolsList.setAdapter(adapter);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            reference.addChildEventListener(mChildEventListener);
            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
            findViewById(R.id.white2).setVisibility(View.GONE);
        } else if (accountType2.equals("student")) { //If the user is a student, add the schools to the Array List
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    if (userProfile.getAccountType().equals("school")) {
                        schoolNames.add(userProfile.getUserName());


                        // set up the RecyclerView
                        schoolsList.setLayoutManager(new LinearLayoutManager(CreateAccountActivity2.this));
                        adapter = new MyRecyclerViewAdapter(CreateAccountActivity2.this, schoolNames);
                        adapter.setClickListener(CreateAccountActivity2.this);
                        schoolsList.setAdapter(adapter);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            reference.addChildEventListener(mChildEventListener);
            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
            findViewById(R.id.white2).setVisibility(View.GONE);
        }

        //Back btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToCreate1 = new Intent(CreateAccountActivity2.this, CreateAccountActivity1.class);
                startActivity(backToCreate1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        //Default center
        center = "Salesians Rocafort";

        //Search
        searchEt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    //When an item of the list is clicked, update the center String, send it to the next activity and go to the SignUpActivity
    @Override
    public void onItemClick(View view, int position) {
        Intent temporal = new Intent(CreateAccountActivity2.this, SignUpActivity.class);
        //Send user account
        temporal.putExtra("accountType2", accountType2);
        center = schoolNames.get(position);
        temporal.putExtra("center", center);
        startActivity(temporal);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        Intent backToCreate1 = new Intent(CreateAccountActivity2.this, CreateAccountActivity1.class);
        startActivity(backToCreate1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
