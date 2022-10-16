package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccountProgress extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_progress);

        //Get info
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        //Sign out
        FirebaseAuth.getInstance().signOut();

        //Go to first Activity
        Intent goToFirst = new Intent(DeleteAccountProgress.this, FirstActivity.class);
        startActivity(goToFirst);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        //Delete account
        FirebaseDatabase.getInstance().getReference("Users").child(userID).removeValue();
    }

    @Override
    public void onBackPressed() {
    }

}
