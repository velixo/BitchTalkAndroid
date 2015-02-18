package com.velixo.bitchtalkandroid.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.command.clientside.Macro;
import com.velixo.bitchtalkandroid.entities.SettingsAdapter;
import com.velixo.bitchtalkandroid.options.CreateMacro;
import com.velixo.bitchtalkandroid.options.Option;
import com.velixo.bitchtalkandroid.options.VolumeSettings;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

public class SettingsFragment extends Fragment {
    public static final String TAG = SettingsFragment.class.getSimpleName();
    private SettingsAdapter adapter;
    private ListView listView;
    private Client client;

    public SettingsFragment() {

    }

    public void addClient(Client client) {
        this.client = client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        List<Option> options = new ArrayList<Option>();
        options.add(new VolumeSettings(client));
        options.add(new CreateMacro(client));

        List<Macro> macros = loadMacros();

        adapter = new SettingsAdapter(getActivity(), client, options, macros);
        listView = (ListView) rootView.findViewById(R.id.settings_listview);
        listView.setAdapter(adapter);
        loadThingsFromState(savedInstanceState);
        return rootView;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return null;
//    }

    private void loadThingsFromState(Bundle savedInstanceState) {
        //TODO implement
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO implement
    }

    private List<Macro> loadMacros() {
        //TODO implement correctly, loading from files and such
        List<Macro> macros = new ArrayList<Macro>();
        macros.add(new Macro("bucken", "/connect buckensrovhal.noip.me"));
        macros.add(new Macro("ls", "/bitchlist"));
        macros.add(new Macro("wl", "/woolooloo"));
        macros.add(new Macro("HeresJohnny", "/open"));
        macros.add(new Macro("hej", "hej"));
        macros.add(new Macro("bajs", "bajs"));

        return macros;
    }
}
