package com.velixo.bitchtalkandroid.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.command.clientside.Macro;
import com.velixo.bitchtalkandroid.entities.OnMacrosChangedListener;
import com.velixo.bitchtalkandroid.entities.ViewPagerAdapter;
import com.velixo.bitchtalkandroid.fragments.ChatFragment;
import com.velixo.bitchtalkandroid.fragments.SettingsFragment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private ChatFragment chatFragment;
    private SettingsFragment settingsFragment;
    private Client client;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private String macroFileName = "macros.txt";
    private OnMacrosChangedListener onMacrosChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragments(savedInstanceState);
        client = new Client(chatFragment, this);
        chatFragment.addClient(client);
        settingsFragment.addClient(client);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);
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

    public void saveMacro(Macro macro) {
        List<Macro> macros = loadMacros();
        macros.add(macro);
        try {
            saveMacrosToFile(macros);
            macrosChanged();
        } catch (IOException e) {
            chatFragment.showSilentMessage("Could not save " + macro.getKey());
        }
    }

    public void replaceMacro(Macro oldMacro, Macro newMacro) {
        List<Macro> macros = loadMacros();
        macros.remove(oldMacro);
        macros.add(newMacro);
        try {
            saveMacrosToFile(macros);
            macrosChanged();
        } catch (IOException e) {
            chatFragment.showSilentMessage("Could not replace " + oldMacro.getKey() + " with " + newMacro.getKey());
        }
    }

    public void deleteMacro(Macro macro) {
        List<Macro> macros = loadMacros();
        macros.remove(macro);
        try {
            saveMacrosToFile(macros);
            macrosChanged();
        } catch (IOException e) {
            chatFragment.showSilentMessage("Could not delete " + macro.getKey());
        }
    }

    private void saveMacrosToFile(List<Macro> macros) throws IOException {
        ObjectOutput outputStream = new ObjectOutputStream(openFileOutput(macroFileName, Context.MODE_PRIVATE));
        outputStream.writeObject(macros);
    }

    public List<Macro> loadMacros() {
        List<Macro> macros = new ArrayList<Macro>();
        Object readObject = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(openFileInput(macroFileName));
            readObject = inputStream.readObject();
            macros = (List<Macro>) readObject;
        } catch (IOException e) {
            chatFragment.showSilentMessage("Ya got a problems loading macros, bitch.");
        } catch (ClassNotFoundException e) {
            chatFragment.showSilentMessage("Ya got a big problem loading macros, bitch.");
            if (readObject != null)
                Log.d("BitchTalk", "MainActivity.loadMacros() -> ClassNotFoundException: readObject.get" + readObject.getClass().getSimpleName());
            else
                Log.d("BitchTalk", "MainActivity.loadMacros() -> ClassNotFoundException: readObject == null");
        }
        //TODO implement
        return macros;
    }

    public void setOnMacrosChangedListener(OnMacrosChangedListener listener) {
        onMacrosChangeListener = listener;
    }

    private void macrosChanged() {
        onMacrosChangeListener.onMacrosChanged();
        Log.d("BitchTalk", "MainActivity.macrosChanged()");
    }
}
