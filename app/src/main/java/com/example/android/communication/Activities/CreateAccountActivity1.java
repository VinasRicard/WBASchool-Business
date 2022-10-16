package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.android.communication.R;

import java.util.Locale;

public class CreateAccountActivity1 extends AppCompatActivity {

    private ImageView backBtn;
    private ImageButton schoolBtn, studentBtn, companyBtn, workerBtn;
    private String accountType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account1);

        //Find views
        backBtn = findViewById(R.id.backBtn);
        schoolBtn = findViewById(R.id.schoolBtn);
        studentBtn = findViewById(R.id.studentBtn);
        companyBtn = findViewById(R.id.companyBtn);
        workerBtn = findViewById(R.id.workerBtn);

        //Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToFirst = new Intent(CreateAccountActivity1.this, FirstActivity.class);
                startActivity(backToFirst);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        //Check the language of the device and change the images
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("english")) {
        } else if (language.equals("català")) {
            studentBtn.setBackgroundResource(R.drawable.estudiant_btn);
            schoolBtn.setBackgroundResource(R.drawable.escola_btn);
            companyBtn.setBackgroundResource(R.drawable.empresa_btn);
            workerBtn.setBackgroundResource(R.drawable.treballador_btn);
        } else if (language.equals("español")) {
            studentBtn.setBackgroundResource(R.drawable.estudiante_btn);
            schoolBtn.setBackgroundResource(R.drawable.escuela_btn);
            companyBtn.setBackgroundResource(R.drawable.empresa_btn);
            workerBtn.setBackgroundResource(R.drawable.trabajador_btn);

        }

        //School Button
        //Go to CreateAccountActivity2 and send the accountType
        schoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "school";
                Intent goToCreate2 = new Intent(CreateAccountActivity1.this, CreateAccountActivity2.class);
                goToCreate2.putExtra("accountType", accountType);
                startActivity(goToCreate2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //Student Button
        //Go to CreateAccountActivity2 and send the accountType
        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "student";
                Intent goToCreate2 = new Intent(CreateAccountActivity1.this, CreateAccountActivity2.class);
                goToCreate2.putExtra("accountType", accountType);
                startActivity(goToCreate2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //Company Button
        //Go to CreateAccountActivity2 and send the accountType
        companyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "company";
                Intent goToCreate2 = new Intent(CreateAccountActivity1.this, CreateAccountActivity2.class);
                goToCreate2.putExtra("accountType", accountType);
                startActivity(goToCreate2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //Worker Button
        //Go to CreateAccountActivity2 and send the accountType
        workerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "worker";
                Intent goToCreate2 = new Intent(CreateAccountActivity1.this, CreateAccountActivity2.class);
                goToCreate2.putExtra("accountType", accountType);
                startActivity(goToCreate2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    //Device Back Btn
    @Override
    public void onBackPressed() {
        Intent backToFirst = new Intent(CreateAccountActivity1.this, FirstActivity.class);
        startActivity(backToFirst);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
