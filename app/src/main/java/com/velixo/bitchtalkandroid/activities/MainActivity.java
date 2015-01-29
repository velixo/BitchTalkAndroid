package com.velixo.bitchtalkandroid.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.entities.SectionsPagerAdapter;
import com.velixo.bitchtalkandroid.fragments.ChatFragment;
import com.velixo.bitchtalkandroid.fragments.SettingsFragment;

public class MainActivity extends ActionBarActivity {
    private ChatFragment chatFragment;
    private SettingsFragment settingsFragment;
    private Client client;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragments(savedInstanceState);
        client = new Client(chatFragment, this);
        chatFragment.addClient(client);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    /**
     * Loads previous fragments if activity was previously open.
     * If the app was started for the first time (since reboot), create new fragments.
     */
    private void loadFragments(Bundle savedInstanceState) {
        FragmentManager fragManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            chatFragment = (ChatFragment) fragManager.getFragment(savedInstanceState, ChatFragment.TAG);
            settingsFragment = (SettingsFragment) fragManager.getFragment(savedInstanceState, SettingsFragment.TAG);
        } else {
            chatFragment = new ChatFragment();
            settingsFragment = new SettingsFragment();
        }
    }

    /**
     * Save the chatting and settings fragments.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        FragmentManager fragManager = getSupportFragmentManager();
        fragManager.putFragment(outState, ChatFragment.TAG, chatFragment);
        fragManager.putFragment(outState, SettingsFragment.TAG, settingsFragment);
    }

    public ChatFragment getChatFragment() {
        return chatFragment;
    }

    public SettingsFragment getSettingsFragment() {
        return settingsFragment;
    }


}
