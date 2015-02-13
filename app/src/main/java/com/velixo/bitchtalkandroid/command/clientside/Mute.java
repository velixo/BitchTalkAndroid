package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.clientSide.ClientGui;
import shared.command.Command;


public class Mute implements Command {
    private static final long serialVersionUID = 213503265396113746L;

    @Override
    public void clientRun(Client c) {
        ClientGui gui = c.getGui();
        gui.setMuteNotificationSound(true);
        gui.showSilentMessage("Notification sound muted, bitch.");
    }

    @Override
    public void clientRunRecieved(Client c) {

    }
}
