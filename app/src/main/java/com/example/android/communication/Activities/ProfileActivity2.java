package com.example.android.communication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.Classes.CenterProfile;
import com.example.android.communication.Classes.UserProfile;
import com.example.android.communication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity2 extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference2;
    private ImageView backBtn;
    private TextView cityTv, countryTv, mailTv, nameTv, personTv, numberTv, changePassword, deleteAccount;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        //Views
        backBtn = findViewById(R.id.backBtnProfile);
        cityTv = (TextView) findViewById(R.id.cityTv);
        countryTv = (TextView) findViewById(R.id.countryTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        mailTv = (TextView) findViewById(R.id.mailTv);
        personTv = (TextView) findViewById(R.id.personTv);
        numberTv = (TextView) findViewById(R.id.numberTv);
        changePassword = findViewById(R.id.changePasswordTv);
        deleteAccount = findViewById(R.id.deleteAccount);

        //Action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.profileOption);
        actionbar.setDisplayHomeAsUpEnabled(false);

        //Back Btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(ProfileActivity2.this, MainActivity2.class);
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
                if (user.getAccountType().equals("school")) {
                    reference2 = FirebaseDatabase.getInstance().getReference("Schools").child(firebaseUser.getUid());
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CenterProfile school = dataSnapshot.getValue(CenterProfile.class);
                            nameTv.setText(school.getName().toString().toUpperCase());
                            cityTv.setText(school.getCity().toString());
                            countryTv.setText(school.getCountry().toString());
                            mailTv.setText(school.getEmail().toString());
                            email = school.getEmail().toString();
                            personTv.setText(school.getPersonContact().toString());
                            numberTv.setText(school.getPhoneContact().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else if (user.getAccountType().equals("company")) {
                    reference2 = FirebaseDatabase.getInstance().getReference("Companies").child(firebaseUser.getUid());
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CenterProfile company = dataSnapshot.getValue(CenterProfile.class);
                            nameTv.setText(company.getName().toUpperCase());
                            cityTv.setText(company.getCity());
                            countryTv.setText(company.getCountry());
                            mailTv.setText(company.getEmail());
                            email = company.getEmail().toString();
                            personTv.setText(company.getPersonContact());
                            numberTv.setText(company.getPhoneContact().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Reset password
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity2.this, getString(R.string.resetPasswordToast), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        //Delete account
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteAccountMail = new Intent(Intent.ACTION_SENDTO);
                deleteAccountMail.setData(Uri.parse("mailto: support@wba.school&business.com")); // only email apps should handle this
                deleteAccountMail.putExtra(deleteAccountMail.EXTRA_SUBJECT, "WBA School DELETE ACCOUNT");
                startActivity(deleteAccountMail);
            }
        });
    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(ProfileActivity2.this, MainActivity2.class);
        startActivity(backToMain);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
