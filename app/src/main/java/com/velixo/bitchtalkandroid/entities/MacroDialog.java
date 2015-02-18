package com.velixo.bitchtalkandroid.entities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.velixo.bitchtalkandroid.R;

/**
 * Created by Vilhelm on 2015-02-17.
 */
public class MacroDialog extends Dialog {
    public final static String CREATE_MACRO_TITLE = "Create macro";
    public final static String EDIT_MACRO_TITLE = "Edit macro";
    private String dialogTitle;
    private String macroName;
    private String commandText;

    public MacroDialog(Context context, String dialogTitle, String macroName, String commandText) {
        super(context);
        this.dialogTitle = dialogTitle;
        this.macroName = macroName;
        this.commandText = commandText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macro_dialog);
        EditText macroNameEditText = (EditText) findViewById(R.id.macro_name_edit_text);
        EditText macroCommandEditText = (EditText) findViewById(R.id.macro_command_edit_text);
        setTitle(dialogTitle);
        macroNameEditText.setText(macroName);
        macroCommandEditText.setText(commandText);
    }
}
