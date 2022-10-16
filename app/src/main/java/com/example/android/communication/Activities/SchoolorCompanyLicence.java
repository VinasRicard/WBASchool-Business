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

public class SchoolorCompanyLicence extends AppCompatActivity {

    EditText nameEt, cityEt, mailEt, countryEt, personEt, phoneEt;
    TextView titleLicence;
    ImageView backBtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolcompany_licence);

        //Get user accountType
        Intent intent = getIntent();
        final String accountType3 = intent.getStringExtra("accountType2");

        //Views
        nameEt = findViewById(R.id.centerNameEt);
        cityEt = findViewById(R.id.centerCityEt);
        mailEt = findViewById(R.id.centerMailEt);
        countryEt = findViewById(R.id.centerCountryEt);
        personEt = findViewById(R.id.personNameEt);
        phoneEt = findViewById(R.id.centerPhone);
        backBtn5 = findViewById(R.id.backBtn5);
        titleLicence = findViewById(R.id.titleLicence);
        Button SendMail = findViewById(R.id.sendMailBtn);

        //Back Btn
        backBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToCreate1 = new Intent(SchoolorCompanyLicence.this, CreateAccountActivity1.class);
                startActivity(backToCreate1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        //Change TextViews depending on the accountType
        if (accountType3.equals("company")) {
            titleLicence.setText(getString(R.string.titleCompany));
            nameEt.setHint(getString(R.string.companyName));
        }

        //SendMail Btn
        SendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameEt.getText().toString()) || TextUtils.isEmpty(cityEt.getText().toString()) || TextUtils.isEmpty(mailEt.getText().toString()) ||
                        TextUtils.isEmpty(countryEt.getText().toString()) || TextUtils.isEmpty(personEt.getText().toString()) || TextUtils.isEmpty(phoneEt.getText().toString())) {
                    Toast.makeText(SchoolorCompanyLicence.this, getString(R.string.pleaseComplete), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto: ricar.vinas@gmail.com")); // only email apps should handle this
                    if (accountType3.equals("school")) {
                        intent.putExtra(intent.EXTRA_SUBJECT, "WBA School");
                    } else {
                        intent.putExtra(intent.EXTRA_SUBJECT, "WBA Company");
                    }
                    intent.putExtra(intent.EXTRA_TEXT, "Name of the " + accountType3 + ": " + nameEt.getText() + "\n"
                            + "City: " + cityEt.getText() + "\n"
                            + "Mail adress: " + mailEt.getText() + "\n"
                            + "Country: " + countryEt.getText() + "\n"
                            + " " + "\n"
                            + "Person to contact: " + personEt.getText() + "\n"
                            + "Phone number to contact: " + phoneEt.getText());
                    if (intent.resolveActivity(getPackageManager()) != null) ;
                    {
                        startActivity(intent);
                    }
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }

            }
        });
    }

    //Device Back Btn
    @Override
    public void onBackPressed() {
        Intent backToCreate1 = new Intent(SchoolorCompanyLicence.this, CreateAccountActivity1.class);
        startActivity(backToCreate1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
