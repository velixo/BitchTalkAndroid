package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.clientSide.ClientGui;
import shared.command.Command;


public class Unmute implements Command {
    private static final long serialVersionUID = 480681111321782893L;

    @Override
    public void clientRun(Client c) {
        ClientGui gui = c.getGui();
        gui.setMuteNotificationSound(false);
        gui.showSilentMessage("Notification sound unmuted, bitch.");
    }

    @Override
    public void clientRunRecieved(Client c) {

    }
}
