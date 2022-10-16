package com.example.android.communication.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.android.communication.Adapters.MessageAdapter;
import com.example.android.communication.Adapters.MyRecyclerViewAdapter;
import com.example.android.communication.Classes.Chat;
import com.example.android.communication.Classes.FolderItem;
import com.example.android.communication.Classes.UserProfile;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoldersActivity2 extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    RecyclerView recyclerView2;
    List<Chat> mChats2;
    List<String> mUsers2;
    List<String> mSeen;
    ImageView backBtn2;
    private FirebaseDatabase firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference2, reference3;
    String folderName, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders2);

        //Views
        recyclerView2 = findViewById(R.id.folders2RV);
        backBtn2 = findViewById(R.id.backBtnFolders2);

        //Get folder name
        final Intent intent = getIntent();
        folderName = intent.getStringExtra("folderName");

        //Action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(folderName);
        actionbar.setDisplayHomeAsUpEnabled(false);

        //School/Company name
        //Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference3 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                userName = user.getUserName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Folders List
        mChats2 = new ArrayList<>();
        mUsers2 = new ArrayList<>();
        mSeen = new ArrayList<>();
        //Firebase
        try {
            firebaseAuth = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Folders").child(folderName);
            ChildEventListener mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final FolderItem folderItem = dataSnapshot.getValue(FolderItem.class);
                    //Look for chats with the receiver and receiver ID
                    reference2 = FirebaseDatabase.getInstance().getReference("Chats");
                    ChildEventListener mChildEventListener2 = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            if (chat.getSender().equals(folderItem.getReceiver()) && chat.getID1().equals(folderItem.getReceiverID()) && chat.getReceiver().equals(userName)) {
                                List<Chat> mMessages;
                                if (!mUsers2.contains(chat.getID1() + chat.getSender())) {
                                    mChats2.add(chat);
                                    mUsers2.add(chat.getID1() + chat.getSender());
                                    MessageAdapter messageAdapter = new MessageAdapter(FoldersActivity2.this, mChats2, "allmessages", mSeen);
                                    recyclerView2.setAdapter(messageAdapter);
                                }
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
                    reference2.addChildEventListener(mChildEventListener2);
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

        }

        //Recycle View
        recyclerView2.setLayoutManager(new LinearLayoutManager(FoldersActivity2.this));

        //Back Btn
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(FoldersActivity2.this, FoldersActivity.class);
                startActivity(backToMain);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(FoldersActivity2.this, FoldersActivity.class);
        startActivity(backToMain);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
