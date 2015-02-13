package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;
import shared.command.Command;


public class Connect implements Command{
    private static final long serialVersionUID = -3359363378018918205L;
    private String address;

    public Connect(String ip){
        address = ip;
    }

    @Override
    public void clientRun(Client c) {
        c.connect(address);
    }

    @Override
    public void clientRunRecieved(Client c) {

    }

}
