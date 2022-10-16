package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextView signUpBtn;
    private EditText emailEt, passwordEt;
    private String email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Firebase
        auth = FirebaseAuth.getInstance();

        //Views
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        signInBtn = findViewById(R.id.signinBtn);
        signUpBtn = findViewById(R.id.goTosignUpBtn);


        //Sign in button
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEt.getText().toString();
                password = passwordEt.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, getString(R.string.pleaseComplete), Toast.LENGTH_SHORT).show();
                } else {

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignInActivity.this, getString(R.string.authFailed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        //Sign up btn
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(SignInActivity.this, CreateAccountActivity1.class);
                startActivity(goToSignUp);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(SignInActivity.this, FirstActivity.class);
        startActivity(backToMain);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
