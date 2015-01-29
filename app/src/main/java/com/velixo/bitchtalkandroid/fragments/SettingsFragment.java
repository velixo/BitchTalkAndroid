package com.velixo.bitchtalkandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.velixo.bitchtalkandroid.R;

public class SettingsFragment extends Fragment {
    public static final String TAG = SettingsFragment.class.getSimpleName();


    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        loadThingsFromState(savedInstanceState);
        return rootView;
    }

    private void loadThingsFromState(Bundle savedInstanceState) {
        //TODO implement
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO implement
    }
}
