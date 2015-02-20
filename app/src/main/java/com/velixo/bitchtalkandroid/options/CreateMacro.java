package com.velixo.bitchtalkandroid.options;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.activities.MainActivity;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.command.clientside.Macro;

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
        final MainActivity activity = (MainActivity) ((Fragment) client.getGui()).getActivity();
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.macro_dialog, null);

        //set up the dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Create macro");
        dialog.setView(dialogView);

        //set the buttons of the dialog
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get the views in the macro dialog
                EditText nameEditText = (EditText) dialogView.findViewById(R.id.macro_name_edit_text);
                EditText commandEditText = (EditText) dialogView.findViewById(R.id.macro_command_edit_text);
                CheckBox returnToChatCheckBox = (CheckBox) dialogView.findViewById(R.id.macro_return_to_chat_check_box);

                //create a new macro and save it
                String macroName = nameEditText.getText().toString();
                String macroCommand = commandEditText.getText().toString();
                boolean returnToChat = returnToChatCheckBox.isChecked();
                Macro macro = new Macro(macroName, macroCommand, returnToChat);
                activity.saveMacro(macro);
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        dialog.show();
    }
}
