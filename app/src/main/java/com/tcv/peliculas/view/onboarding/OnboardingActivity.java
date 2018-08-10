package com.tcv.peliculas.view.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tcv.peliculas.R;
import com.tcv.peliculas.view.CategoriasActivity;

public class OnboardingActivity extends FragmentActivity {
    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button skip;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        pager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return new OnboardingFragment1();
                    case 1 : return new OnboardingFragment2();
                    case 2 : return new OnboardingFragment3();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pager.getCurrentItem() == 2) {
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(
                            pager.getCurrentItem() + 1,
                            true
                    );
                }
            }
        });

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    skip.setVisibility(View.GONE);
                    next.setText("Entrar!");
                }
                else {
                    skip.setVisibility(View.VISIBLE);
                    next.setText("Siguiente");
                }

            }
        });
    }

    private void finishOnboarding() {
        Intent main = new Intent(this, CategoriasActivity.class);
        startActivity(main);
        finish();
    }
}