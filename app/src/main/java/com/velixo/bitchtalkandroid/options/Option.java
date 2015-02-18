package com.velixo.bitchtalkandroid.options;

import android.app.Dialog;

/**
 * Created by Vilhelm on 2015-02-16.
 */
public abstract class Option {
    protected String name;

    public String getName() {
        return name;
    }

    public abstract void onClick();
}
