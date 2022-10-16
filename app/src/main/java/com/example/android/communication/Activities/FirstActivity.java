package com.example.android.communication.Activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.Adapters.ImageAdapter;
import com.example.android.communication.R;

import java.util.Collection;
import java.util.Locale;

public class FirstActivity extends AppCompatActivity {


    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    ViewPager viewPager;
    private TextView signInBtn;
    private Button getStartedBtn;
    int[] sources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //Views
        viewPager = findViewById(R.id.viewPager);
        signInBtn = findViewById(R.id.goTosignUpBtn);
        getStartedBtn = findViewById(R.id.getStartedBtn);

        //Check the language and change the images depending on it
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English") || language.equals("english")) {
            sources = new int[]{R.drawable.p_wba, R.drawable.p_stopit, R.drawable.p_completely_anonymous};
        } else if (language.equals("español")) {
            sources = new int[]{R.drawable.p_wba_e, R.drawable.p_detenlo, R.drawable.p_completamente_anonimo};
        } else if (language.equals("català")) {
            sources = new int[]{R.drawable.p_wba_c, R.drawable.p_aturaho, R.drawable.p_completament_anonim};
        }


        //Set up the image slider
        ImageAdapter adapter = new ImageAdapter(this, sources);
        viewPager.setAdapter(adapter);
        //Set up the dots indicator of the Image Slider
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        dotscount = adapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        //SignIn btn
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(FirstActivity.this, SignInActivity.class);
                startActivity(signInActivity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //Get Started Button
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getStarted = new Intent(FirstActivity.this, CreateAccountActivity1.class);
                startActivity(getStarted);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    //Device BackBtn
    @Override
    public void onBackPressed() {

    }
}
