package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.statics.StaticVariables;

import shared.command.Command;

public class Help implements Command {
    private static final long serialVersionUID = 5833972601159022991L;

    @Override
    public void clientRun(Client c) {
        String help = "Bitch needed some commands?\n" +
                StaticVariables.HELP + "\n" +
                StaticVariables.BARKS + "\n" +
                StaticVariables.CONNECT + "\n" +
                StaticVariables.MUTE + "\n" +
                StaticVariables.UNMUTE + "\n" +
                StaticVariables.BITCHLIST + "\n" +
                StaticVariables.SETNAME + "\n" +
                StaticVariables.MACRO + " <keyword> <commands to macro separated by spaces.>\n";
        c.getGui().showSilentMessage(help);
    }

    @Override
    public void clientRunRecieved(Client c) {

    }
}
