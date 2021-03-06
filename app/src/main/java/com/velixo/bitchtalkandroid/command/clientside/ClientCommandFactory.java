package com.velixo.bitchtalkandroid.command.clientside;

import android.content.Context;

import com.velixo.bitchtalkandroid.statics.StaticVariables;

import java.io.IOException;
import java.util.StringTokenizer;

import shared.command.Command;
import shared.command.Disconnect;
import shared.command.Message;
import shared.command.NotACommand;
import shared.command.Sound;
import shared.command.UnrecognizedCommand;


public class ClientCommandFactory {
    public final static String HELP = StaticVariables.HELP;
    public final static String BARKS= StaticVariables.BARKS;
    public final static String MUTE = StaticVariables.MUTE;
    public final static String UNMUTE = StaticVariables.UNMUTE;
    public final static String CONNECT = StaticVariables.CONNECT;
    public final static String DISCONNECT = StaticVariables.DISCONNECT;
    public final static String MACRO = StaticVariables.MACRO;
    private final static String NOT_A_SOUND = "NOT_A_SOUND";

    public static String help(){
        return "type /connect <ip-address> to connect and /help for help, bitch.";
    }

    public static Command build(String input, Context context) {
        StringTokenizer st = new StringTokenizer(input);
        switch (st.nextToken()) {
            case HELP:
                return new Help();

            case BARKS:
                return new Barks();

            case MUTE:
                return new Mute();

            case UNMUTE:
                return new Unmute();

            case CONNECT:
                if(st.hasMoreTokens())
                    return new Connect(st.nextToken());
                else
                    return new NotACommand();

            case DISCONNECT:
                return new Disconnect();

            case MACRO:
                String key;
                if(st.hasMoreTokens())
                    key = st.nextToken();
                else
                    return new NotACommand();
                if(st.hasMoreTokens()){
                    StringBuilder sb = new StringBuilder();
                    while(st.hasMoreTokens()){
                        sb.append(st.nextToken() + " ");
                    }
                    return new Macro(key,sb.toString());
                }
                else
                    return new NotACommand();
            default:
                String soundName = getSoundName(input, context);
                if(!soundName.equals(NOT_A_SOUND)) {
                    return new Sound(soundName);
                } else if(isPossibleCommand(input))
                    return new UnrecognizedCommand(input);
                return new Message(input);
        }
    }

    private static boolean isPossibleCommand(String input) {
        return input.charAt(0) == '/';
    }

    private static String getSoundName(String input, Context context) {
        String normalSoundName = input.replace("/", "");
        String adminSoundName = input.replace("/", "admin_");
        String otherSoundName = input.replace("/", "other_");
        String hiddenSoundName = input.replace("/", "hidden_");

        String[] soundNames = {normalSoundName, adminSoundName, otherSoundName, hiddenSoundName};
        for (String soundName : soundNames) {
            if(soundExists(soundName, context))
                return soundName;
        }

        return NOT_A_SOUND;
    }

    private static boolean soundExists(String soundName, Context context) {
        try {
            String[] sounds = context.getAssets().list("sounds");
            for (String sound : sounds){
                if(sound.equals(soundName + ".wav"))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
