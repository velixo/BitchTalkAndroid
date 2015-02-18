package com.velixo.bitchtalkandroid.options;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.entities.MacroDialog;

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
    public void onClick() {
        Context context = ((Fragment) client.getGui()).getActivity();
        Dialog dialog = new MacroDialog(context, MacroDialog.CREATE_MACRO_TITLE, "", "");
        dialog.show();
    }
}
