package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.Client;

import java.util.HashMap;

import shared.command.Command;

public class Macro implements Command{
    private String key;
    private String command;
    private boolean returnToChat;

    public Macro(String key,String command){
        this.key = key;
        this.command = command.replaceAll(" /","+/");
        returnToChat = false;
    }

    public Macro(String key,String command, boolean returnToChat){
        this.key = key;
        this.command = command.replaceAll(" /","+/");
        this.returnToChat = returnToChat;
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

    public String getKey() {
        return key;
    }

    public String getCommand() {
        return command;
    }

    public boolean getReturnToChat() {
        return returnToChat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Macro) {
            Macro macro = (Macro) obj;
            return key.equals(macro.key)
                && command.equals(macro.command)
                && returnToChat == macro.returnToChat;
        }
        return false;
    }
}
