package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.communication.Adapters.ChatAdapter;
import com.example.android.communication.Adapters.FoldersAdapter;
import com.example.android.communication.Adapters.MyRecyclerViewAdapter;
import com.example.android.communication.Classes.Chat;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoldersActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    RecyclerView recyclerView;
    List<String> mFolders;
    FoldersAdapter foldersAdapter;
    ImageView backBtn;
    private FirebaseDatabase firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        //Views
        recyclerView = findViewById(R.id.foldersRV);
        backBtn = findViewById(R.id.backBtnFolders);

        //Action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getString(R.string.foldersOption));
        actionbar.setDisplayHomeAsUpEnabled(false);

        //Folders List
        mFolders = new ArrayList<>();
        //Firebase
        try {
            firebaseAuth = FirebaseDatabase.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Folders");
            ChildEventListener mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //Get the names of the folders and add them to the ArrayList
                    String folderName = dataSnapshot.getKey();
                    mFolders.add(folderName);
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
        } catch (Exception E) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        //Recycle View
        recyclerView.setLayoutManager(new LinearLayoutManager(FoldersActivity.this));
        foldersAdapter = new FoldersAdapter(FoldersActivity.this, mFolders);
        recyclerView.setAdapter(foldersAdapter);

        //Back Btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(FoldersActivity.this, MainActivity.class);
                startActivity(backToMain);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        findViewById(R.id.loadingPanel3).setVisibility(View.GONE);
        findViewById(R.id.white3).setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(FoldersActivity.this, MainActivity.class);
        startActivity(backToMain);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
