package com.velixo.bitchtalkandroid.options;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.velixo.bitchtalkandroid.R;
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
    public void onClick() {
        ActionBarActivity activity = (ActionBarActivity) ((Fragment) client.getGui()).getActivity();
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Create macro");
        dialog.setView(activity.getLayoutInflater().inflate(R.layout.macro_dialog, null));
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO implement
                //save Macro
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO implement
                //discard any changes
            }
        });
        dialog.show();
    }
}
