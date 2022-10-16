package com.example.android.communication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity {


    private ImageView backBtn;
    private TextView signIn;
    private EditText emailEt, passwordEt;
    private String emailSt, passwordSt;
    private Button signUp;
    FirebaseAuth auth;
    DatabaseReference reference;
    private String accountType3;
    private String center2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Views
        backBtn = findViewById(R.id.backBtn);
        emailEt = findViewById(R.id.emailEt2);
        passwordEt = findViewById(R.id.passwordEt2);
        signUp = findViewById(R.id.signupBtn);
        signIn = findViewById(R.id.goTosignUpBtn);

        //Get info
        Intent intent = getIntent();
        accountType3 = intent.getStringExtra("accountType2");
        center2 = intent.getStringExtra("center");

        //Back Btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountType3.equals("school") | accountType3.equals("company")) {
                    Intent backToCreate2 = new Intent(SignUpActivity.this, CreateAccountActivity1.class);
                    startActivity(backToCreate2);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Intent backToCreate2 = new Intent(SignUpActivity.this, CreateAccountActivity2.class);
                    backToCreate2.putExtra("accountType", accountType3);
                    startActivity(backToCreate2);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        //Sign up btn
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailSt = emailEt.getText().toString();
                passwordSt = passwordEt.getText().toString();
                if (TextUtils.isEmpty(emailSt) || TextUtils.isEmpty(passwordSt)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.pleaseComplete), Toast.LENGTH_SHORT).show();
                } else if (passwordSt.length() < 6) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.characters6), Toast.LENGTH_SHORT).show();
                } else {
                    Intent goToCreateAccount4 = new Intent(SignUpActivity.this, CreateAccountActivity4.class);
                    goToCreateAccount4.putExtra("adress", emailSt);
                    goToCreateAccount4.putExtra("password", passwordSt);
                    goToCreateAccount4.putExtra("accountType3", accountType3);
                    goToCreateAccount4.putExtra("center2", center2);
                    startActivity(goToCreateAccount4);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        //Sign in btn
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignInActivity = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(goToSignInActivity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        if (accountType3.equals("school") | accountType3.equals("company")) {
            Intent backToCreate2 = new Intent(SignUpActivity.this, CreateAccountActivity1.class);
            startActivity(backToCreate2);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            Intent backToCreate2 = new Intent(SignUpActivity.this, CreateAccountActivity2.class);
            backToCreate2.putExtra("accountType", accountType3);
            startActivity(backToCreate2);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

}
