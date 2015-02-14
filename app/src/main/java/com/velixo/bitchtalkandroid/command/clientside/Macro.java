package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;

import java.util.HashMap;

import shared.command.Command;

public class Macro implements Command{

    String key;
    String command;

    public Macro(String key,String command){
        this.key = key;
        this.command = command.replaceAll(" /","+/");
    }
    @Override
    public void clientRun(Client c) {
        HashMap<String,String> map = c.macroMap();

        map.put(key,command);

        c.getGui().showMessage("Bitch, I'll remember that.");
    }

    @Override
    public void clientRunRecieved(Client c) {

    }
}
