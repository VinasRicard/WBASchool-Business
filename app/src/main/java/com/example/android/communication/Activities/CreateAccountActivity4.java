package com.example.android.communication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class CreateAccountActivity4 extends AppCompatActivity {


    private ImageView backBtn;
    private Button signupBtn;
    FirebaseAuth auth;
    DatabaseReference reference;
    private String emailSt, passwordSt;
    private EditText nameEt;
    private String usernameSt;
    private String accountType4;
    private String center3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account4);

        //Views
        backBtn = findViewById(R.id.backBtn);
        signupBtn = findViewById(R.id.signupBtn2);

        //Firebase
        auth = FirebaseAuth.getInstance();

        //BackBtn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToSignUp = new Intent(CreateAccountActivity4.this, CreateAccountActivity1.class);
                startActivity(backToSignUp);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        //Get user info
        Intent intent = getIntent();
        emailSt = intent.getStringExtra("adress");
        passwordSt = intent.getStringExtra("password");
        nameEt = findViewById(R.id.nameEt);
        Intent intent1 = getIntent();
        accountType4 = intent1.getStringExtra("accountType3");
        center3 = intent1.getStringExtra("center2");


        //Sign up Btn
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameSt = nameEt.getText().toString();

                if (TextUtils.isEmpty(usernameSt)) {
                    Toast.makeText(CreateAccountActivity4.this, getString(R.string.writeYourName), Toast.LENGTH_SHORT).show();
                } else {
                    register(usernameSt, emailSt, passwordSt);
                }
            }
        });

        //Terms and conditions
        TextView terms = findViewById(R.id.cond_of_use);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/document/d/1Qv22n6bGgJ9KDKRPMp2igQtQvFHjl8mixOIbpfR7wa0/edit?usp=sharing";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    //Register
    private void register(final String username, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("Name", username);
                            hashMap.put("center", center3);
                            hashMap.put("accountType", accountType4);
                            hashMap.put("name", username.toLowerCase());
                            hashMap.put("email", emailSt);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(CreateAccountActivity4.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {

                                    }

                                }
                            });
                        } else {
                            Toast.makeText(CreateAccountActivity4.this, getString(R.string.cantRegister), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Device BackBtn
    @Override
    public void onBackPressed() {
        Intent backToSignUp = new Intent(CreateAccountActivity4.this, CreateAccountActivity1.class);
        startActivity(backToSignUp);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
