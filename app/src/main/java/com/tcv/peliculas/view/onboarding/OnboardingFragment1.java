package com.tcv.peliculas.view.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tcv.peliculas.R;

public class OnboardingFragment1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle s) {
 
        return inflater.inflate(
          R.layout.onboarding_pantalla1,
          container,
          false
        );
 
    }
}