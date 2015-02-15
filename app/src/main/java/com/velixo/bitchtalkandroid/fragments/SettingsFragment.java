package com.velixo.bitchtalkandroid.fragments;

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
import com.velixo.bitchtalkandroid.command.clientside.Macro;
import com.velixo.bitchtalkandroid.entities.SettingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    public static final String TAG = SettingsFragment.class.getSimpleName();
    private SettingsAdapter adapter;
    private ListView listView;

    public SettingsFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        List<Macro> macroList = new ArrayList<Macro>();
        macroList.add(new Macro("bucken", "/connect buckensrovhal.noip.me"));
        macroList.add(new Macro("ls", "/bitchlist"));
        macroList.add(new Macro("annoy", "/woolooloo /bossassbitch /yee /celebrate"));

        adapter = new SettingsAdapter(getActivity(), macroList);
        listView = (ListView) rootView.findViewById(R.id.settings_listview);
        listView.setAdapter(adapter);
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
