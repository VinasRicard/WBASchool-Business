package com.example.android.communication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
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

public class FolderItsSavedActivity extends AppCompatActivity {

    TextView folderName, changeFolder, deleteFromFolder;
    ImageView closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_its_saved);

        //Pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .5));
        getWindow().setElevation(10);

        //Views
        folderName = findViewById(R.id.folderName);
        changeFolder = findViewById(R.id.changeFolder);
        deleteFromFolder = findViewById(R.id.deleteFromFolder);
        closeBtn = findViewById(R.id.popupCloseBtn);

        //Get info
        final Intent intent = getIntent();
        final String receiverID = intent.getStringExtra("receiverID3");
        final String receiver = intent.getStringExtra("receiver3");
        final String folderNAme = intent.getStringExtra("folder");

        //FolderName
        folderName.setText(folderNAme);

        //Change folder
        changeFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo(receiver, receiverID, folderNAme);
                Intent openAddTo = new Intent(FolderItsSavedActivity.this, FolderAddToActivity.class);
                openAddTo.putExtra("receiverID2", receiverID);
                openAddTo.putExtra("receiver2", receiver);
                startActivity(openAddTo);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        //Delete from folder
        deleteFromFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo(receiver, receiverID, folderNAme);
                finish();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteInfo(final String receiver, final String receiverID, final String folderNAme) {
        //Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = firebaseUser.getUid();

        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Folders").child(folderNAme);
        final ChildEventListener mChildEventListener2 = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String folderItemId = dataSnapshot.getKey();
                final FolderItem folderItem = dataSnapshot.getValue(FolderItem.class);
                if (folderItem.getReceiver().equals(receiver) && folderItem.getReceiverID().equals(receiverID)) {
                    FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Folders").child(folderNAme).child(folderItemId).removeValue();
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
    public void onBackPressed() {
        finish();
    }


}
