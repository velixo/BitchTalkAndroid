package com.velixo.bitchtalkandroid.activities;


import android.os.Bundle;
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
        if (chatFragment == null)
            chatFragment = new ChatFragment();
        if (settingsFragment == null)
            settingsFragment = new SettingsFragment();
        client = new Client(chatFragment, this);
        chatFragment.addClient(client);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    public ChatFragment getChatFragment() {
        return chatFragment;
    }

    public SettingsFragment getSettingsFragment() {
        return settingsFragment;
    }


}
