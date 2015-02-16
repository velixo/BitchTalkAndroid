package com.velixo.bitchtalkandroid.options;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.velixo.bitchtalkandroid.clientSide.Client;

/**
 * Created by Vilhelm on 2015-02-16.
 */
public class CreateMacro extends Option {
    private Client client;

    public CreateMacro(Client client) {
        name = "Add new Macro...";
        this.client = client;
    }


    @Override
    public void execute() {
        openMacroDialog();
    }


    private void openMacroDialog() {
        Context context = ((Fragment) client.getGui()).getActivity();
        Toast.makeText(context, "openMacroDialog", Toast.LENGTH_SHORT);
        //TODO implement
    }
}
