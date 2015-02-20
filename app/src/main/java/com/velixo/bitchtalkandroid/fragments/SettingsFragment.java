package com.velixo.bitchtalkandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.activities.MainActivity;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.command.clientside.Macro;
import com.velixo.bitchtalkandroid.entities.SettingsAdapter;
import com.velixo.bitchtalkandroid.options.CreateMacro;
import com.velixo.bitchtalkandroid.options.Option;
import com.velixo.bitchtalkandroid.options.VolumeSettings;

import java.util.ArrayList;
import java.util.List;

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

        List<Macro> macros = ((MainActivity) getActivity()).loadMacros();

        adapter = new SettingsAdapter(getActivity(), client, options, macros);
        listView = (ListView) rootView.findViewById(R.id.settings_listview);
        listView.setAdapter(adapter);
        ((MainActivity) getActivity()).setOnMacrosChangedListener(adapter);

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
