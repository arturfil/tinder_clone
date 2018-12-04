package com.arturofilio.tinder_clone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    TextView mSignup;
    Button mbtnSingUp;

    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mSignup         = (TextView)     findViewById(R.id.sign_up_link);
        viewPager       = (ViewPager)    findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.slider_dots);
        mbtnSingUp      = (Button)       findViewById(R.id.btn_signin);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.non_active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                
            }

            @Override
            public void onPageSelected(int i) {

                for (int j = 0; j < dotscount; j++) {
                    dots[j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.active_dot));
                }

                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.non_active_dot));
                
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mbtnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            WelcomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else if(viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }

}
