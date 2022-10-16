package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.R;
import com.example.android.communication.Classes.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private ImageView backBtn;
    private TextView profileName, profileEmail, profileCenter, profileAccountType, tvProfile2, resetPassword, editAccount, deleteAccount;
    private String email, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Views
        backBtn = findViewById(R.id.backBtnProfile);
        profileName = (TextView) findViewById(R.id.nameProfile);
        profileEmail = (TextView) findViewById(R.id.adressProfile);
        profileCenter = (TextView) findViewById(R.id.centerProfile);
        profileAccountType = (TextView) findViewById(R.id.accountTypeProfile);
        tvProfile2 = (TextView) findViewById(R.id.tvProfile2);
        resetPassword = (TextView) findViewById(R.id.changePasswordTv);
        editAccount = (TextView) findViewById(R.id.editAccountTv);
        deleteAccount = findViewById(R.id.deleteAccount);

        //Action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.profileOption);
        actionbar.setDisplayHomeAsUpEnabled(false);

        //Back Btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(backToMain);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Get the user information and display it
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                profileCenter.setText(user.getUserCenter());
                if (user.getAccountType().equals("student")) {
                    profileAccountType.setText(getResources().getString(R.string.student));
                } else if (user.getAccountType().equals("worker")) {
                    profileAccountType.setText(getResources().getString(R.string.worker));
                }

                profileName.setText(user.getUserName());
                name = user.getUserName();
                profileEmail.setText(user.getUserEmail());
                email = user.getUserEmail();
                if (user.getAccountType().equals("worker")) {
                    tvProfile2.setText("Company");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Reset password
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, getString(R.string.resetPasswordToast), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        //Edit account
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAccount = new Intent(ProfileActivity.this, changeProfileActivity.class);
                editAccount.putExtra("name", name);
                editAccount.putExtra("email", email);
                startActivity(editAccount);
            }
        });

        //Delete account
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteForever = new Intent(ProfileActivity.this, DeleteAccountPopUp.class);
                startActivity(deleteForever);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(backToMain);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
