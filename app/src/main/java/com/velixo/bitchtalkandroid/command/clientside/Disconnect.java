package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.command.Command;

/**
 * Created by Vilhelm on 2015-01-30.
 */
public class Disconnect implements Command {
    private Client c;
    public Disconnect(Client client) {
        c = client;
    }

    @Override
    public void run() {
        c.closeCrap();
    }
}
