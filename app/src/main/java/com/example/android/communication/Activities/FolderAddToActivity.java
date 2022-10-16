package com.example.android.communication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.Classes.FolderItem;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FolderAddToActivity extends AppCompatActivity {

    private EditText newFolder;
    private Spinner existingFolder;
    private TextView okayBtn;
    private TextView newFolderTv;
    private ImageView close;
    private ArrayList<String> folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_add_to);

        //Pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .5));
        getWindow().setElevation(10);

        //Views
        existingFolder = findViewById(R.id.existingFolderSp);
        newFolder = findViewById(R.id.newFolderEt);
        okayBtn = findViewById(R.id.okBtn);
        newFolderTv = findViewById(R.id.newFolderTv);
        close = findViewById(R.id.popupCloseBtn);

        //New folder hide
        newFolderTv.setVisibility(newFolderTv.INVISIBLE);
        newFolder.setVisibility(newFolder.INVISIBLE);

        //Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = firebaseUser.getUid();

        //Get info
        final Intent intent = getIntent();
        final String receiverID = intent.getStringExtra("receiverID2");
        final String receiver = intent.getStringExtra("receiver2");

        //Check if its saved
        try {
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Folders");
            ChildEventListener mChildEventListener2 = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final String nameFolder = dataSnapshot.getKey();
                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Folders").child(nameFolder);
                    ChildEventListener mChildEventListener3 = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            FolderItem randomFolderItem = dataSnapshot.getValue(FolderItem.class);
                            if (receiverID.equals(randomFolderItem.getReceiverID()) && receiver.equals(randomFolderItem.getReceiver())) {
                                //Open the folderItsSavedActivity pop up window and send the receiver and receiverID to it
                                Intent openItsSaved = new Intent(FolderAddToActivity.this, FolderItsSavedActivity.class);
                                openItsSaved.putExtra("receiverID3", receiverID);
                                openItsSaved.putExtra("receiver3", receiver);
                                openItsSaved.putExtra("folder", nameFolder);
                                startActivity(openItsSaved);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
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
                    reference3.addChildEventListener(mChildEventListener3);
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
        } catch (Exception E2) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        //Existing folders
        folders = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, folders);
        folders.add(" ");
        folders.add("+ NEW FOLDER");

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Folders");
            ChildEventListener mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String folderName = dataSnapshot.getKey();
                    folders.add(folderName);

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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        existingFolder.setAdapter(adapter);


        //OkayBtnClick
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newFolder.getText()) && !existingFolder.getSelectedItem().equals(" ") && !existingFolder.getSelectedItem().equals("+ NEW FOLDER")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Folders").child(existingFolder.getSelectedItem().toString());

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("receiverID", receiverID);
                    hashMap.put("receiver", receiver);

                    reference.push().setValue(hashMap);

                    finish();

                } else if (!TextUtils.isEmpty(newFolder.getText())) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Folders").child(newFolder.getText().toString());

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("receiverID", receiverID);
                    hashMap.put("receiver", receiver);

                    reference.push().setValue(hashMap);

                    finish();
                } else if (TextUtils.isEmpty(newFolder.getText()) && existingFolder.getSelectedItem().equals(" ")) {

                } else if (TextUtils.isEmpty(newFolder.getText()) && existingFolder.getSelectedItem().equals("+ NEW FOLDER")) {

                }
            }
        });

        //SpinnerClicks
        existingFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (existingFolder.getSelectedItem().equals("+ NEW FOLDER")) {
                    newFolderTv.setVisibility(newFolderTv.VISIBLE);
                    newFolder.setVisibility(newFolder.VISIBLE);
                } else {
                    newFolderTv.setVisibility(newFolderTv.INVISIBLE);
                    newFolder.setVisibility(newFolder.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Close
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
