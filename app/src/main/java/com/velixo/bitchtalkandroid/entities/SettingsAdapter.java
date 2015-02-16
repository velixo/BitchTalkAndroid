package com.velixo.bitchtalkandroid.entities;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.command.clientside.Macro;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Vilhelm on 2015-02-15.
 */
public class SettingsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Macro> macros;

    public SettingsAdapter(Context context, List<Macro> macros) {
        this.macros = macros;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return macros.size();
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

            holder.macroNameView = (TextView) convertView.findViewById(R.id.setting_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Macro macro = macros.get(position);
        holder.macroNameView.setText(macro.getKey());
        holder.macroNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement correct functionality for clicks
                Toast.makeText(context, macro.getCommand(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.macroNameView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO implement correct functionality for long presses
                Toast.makeText(context, "LONGPRESS " + macro.getKey(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView macroNameView;
    }


}
