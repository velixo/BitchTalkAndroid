package com.velixo.bitchtalkandroid.command.clientside;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.velixo.bitchtalkandroid.clientSide.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shared.command.Command;

/**
 * Created by Vilhelm on 2015-02-21.
 */
public class Barks implements Command {
    @Override
    public void clientRun(Client c) {
        Context context = ((Fragment) c.getGui()).getActivity();
        List<String> sounds = getNormalSoundNames(context);
        String barks = "So, the bitch needs to bark?\n";
        for(String sound : sounds) {
            barks += "/" + sound + "\n";
        }
        c.getGui().showSilentMessage(barks);
    }

    private List<String> getNormalSoundNames(Context context) {
        List<String> normalSounds = new ArrayList<String>();
        String[] sounds = new String[0];
        try {
            sounds = context.getAssets().list("sounds");
            for(String sound : sounds) {
                if(!sound.contains("other_")
                        && !sound.contains("admin_")
                        && !sound.contains("hidden_")
                        &&  sound.contains(".wav")) {
                    normalSounds.add(sound.replace(".wav", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return normalSounds;
    }

    @Override
    public void clientRunRecieved(Client c) {

    }
}
