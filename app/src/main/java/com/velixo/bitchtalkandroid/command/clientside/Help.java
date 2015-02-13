package com.velixo.bitchtalkandroid.command.clientside;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.velixo.bitchtalkandroid.clientSide.Client;

import shared.command.Command;
import com.velixo.bitchtalkandroid.statics.StaticVariables;

public class Help implements Command {
    private static final long serialVersionUID = 5833972601159022991L;

    @Override
    public void clientRun(Client c) {
        Context context = ((Fragment) c.getGui()).getActivity();
        String help = "\nBitch needed some commands?\n" +
                StaticVariables.HELP + "\n" +
                StaticVariables.CONNECT + "\n" +
                StaticVariables.MUTE + "\n" +
                StaticVariables.UNMUTE + "\n" +
                StaticVariables.BITCHLIST + "\n" +
                StaticVariables.SETNAME + "\n";
        List<String> sounds = getNormalSoundNames(context);
        for(String sound : sounds) {
            help += "/" + sound + "\n";
        }
        c.getGui().showSilentMessage(help);
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
