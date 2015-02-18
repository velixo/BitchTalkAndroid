package com.velixo.bitchtalkandroid.options;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.velixo.bitchtalkandroid.clientSide.Client;

/**
 * Created by Vilhelm on 2015-02-16.
 */
public class VolumeSettings extends Option {
    private Client client;

    public VolumeSettings(Client client) {
        name = "Volume settings";
        this.client = client;
    }

    @Override
    public void onClick() {
        openVolumeSettingsFragment();
    }

    private void openVolumeSettingsFragment() {
        Context context = ((Fragment) client.getGui()).getActivity();
        Toast.makeText(context, "openVolumeSettingsFragment" ,Toast.LENGTH_SHORT).show();
        //TODO implement
    }
}
