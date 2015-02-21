package com.velixo.bitchtalkandroid.entities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.activities.MainActivity;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.command.clientside.Macro;
import com.velixo.bitchtalkandroid.options.Option;

import java.util.List;

/**
 * Created by Vilhelm on 2015-02-15.
 */
public class SettingsAdapter extends BaseAdapter implements OnMacrosChangedListener {
    private Context context;
    private Client client;
    private LayoutInflater inflater;
    private List<Macro> macros;
    private List<Option> options;

    public SettingsAdapter(Context context, Client client,List<Option> options, List<Macro> macros) {
        this.macros = macros;
        this.options = options;
        this.client = client;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return options.size() + macros.size();
    }

    @Override
    public Macro getItem(int position) {
        return macros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            holder.settingView = (TextView) convertView.findViewById(R.id.setting_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < options.size()) {
            setSettingViewAsOption(position, holder);
        } else {
            setSettingViewAsMacro(position - options.size(), holder);
        }
        return convertView;
    }

    private void loadViewHolder(ViewHolder holder, View convertView, ViewGroup parent) {
//        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            holder.settingView = (TextView) convertView.findViewById(R.id.setting_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        return holder;
    }

    /**
     *  Sets the ViewHolder holder up for usage as a option
     *
     *  @param position the position of the requested option in options
     *  @param holder the ViewHolder on which to apply the option functionality on
     * */
    private void setSettingViewAsOption(int position, ViewHolder holder) {
        final Option option = options.get(position);
        holder.settingView.setText(option.getName());
        holder.settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option.onClick();
            }
        });
    }

    /**
     *  Sets the ViewHolder holder up for usage as a macro
     *
     *  @param position the position of the requested macro in macros
     *  @param holder the ViewHolder on which to apply the macro functionality on
     * */
    private void setSettingViewAsMacro(int position, final ViewHolder holder) {
        final Macro macro = macros.get(position);
        holder.settingView.setText(macro.getKey());
        holder.settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(macro.getReturnToChat()) {
                    final MainActivity activity = (MainActivity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.getViewPager().setCurrentItem(ViewPagerAdapter.CHATFRAGMENT);
                        }
                    });
                }
                client.buildAndRunCommand(macro.getCommand());
            }
        });
        holder.settingView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Edit macro");

                //get the macro dialog and the views inside it
                View dialogView = inflater.inflate(R.layout.macro_dialog, null);
                final EditText nameEditText = (EditText) dialogView.findViewById(R.id.macro_name_edit_text);
                final EditText commandEditText = (EditText) dialogView.findViewById(R.id.macro_command_edit_text);
                final CheckBox returnToChatCheckBox = (CheckBox) dialogView.findViewById(R.id.macro_return_to_chat_check_box);

                //set the text of the EditTexts in the macro edit dialog
                nameEditText.setText(macro.getKey());
                commandEditText.setText(macro.getCommand());
                returnToChatCheckBox.setChecked(macro.getReturnToChat());
                dialog.setView(dialogView);

                //set the buttons of the macro editing dialog
                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String macroName = nameEditText.getText().toString();
                        String macroCommand = commandEditText.getText().toString();
                        boolean returnToChat = returnToChatCheckBox.isChecked();
                        Macro newMacro = new Macro(macroName, macroCommand, returnToChat);
                        ((MainActivity) context).deleteMacro(macro);
                        ((MainActivity) context).saveMacro(newMacro);
                    }
                });
                dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() { //show a new dialog checking if the user is sure they want to remove the macro
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder checkIfSureDialog = new AlertDialog.Builder(context);
                        checkIfSureDialog.setMessage("Are you sure you want to delete " + macro.getKey() + "?");
                        checkIfSureDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity) context).deleteMacro(macro);
                            }
                        });
                        checkIfSureDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });
                        checkIfSureDialog.show();
                    }
                });
                dialog.show();
                return true;
            }
        });
        applyMacroStyling(holder);
    }
    private void applyMacroStyling(ViewHolder holder) {
        int vertPadding = holder.settingView.getPaddingTop();
        int leftPadding = (int) (21 * context.getResources().getDisplayMetrics().density);
        holder.settingView.setPadding(leftPadding, vertPadding, 0, vertPadding);
        Typeface tf = holder.settingView.getTypeface();
        holder.settingView.setTypeface(tf, Typeface.ITALIC);
        holder.settingView.setTextColor(Color.parseColor("#888888"));
    }

    @Override
    public void onMacrosChanged() {
        macros = ((MainActivity) context).loadMacros();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView settingView;
    }
}
