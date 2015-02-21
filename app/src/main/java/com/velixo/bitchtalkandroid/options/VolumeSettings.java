package com.velixo.bitchtalkandroid.options;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.activities.MainActivity;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.fragments.VolumeSettingsFragment;

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
        MainActivity activity= (MainActivity) ((Fragment) client.getGui()).getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.main_activity_content, new VolumeSettingsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
