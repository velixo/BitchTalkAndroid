package com.velixo.bitchtalkandroid.options;

/**
 * Created by Vilhelm on 2015-02-16.
 */
public abstract class Option {
    protected String name;

    public String getName() {
        return name;
    }

    public abstract void execute();
}
