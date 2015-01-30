package com.velixo.bitchtalkandroid.command.clientside;

import java.io.IOException;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.clientSide.ClientGui;
import com.velixo.bitchtalkandroid.command.Command;
import com.velixo.bitchtalkandroid.command.NotACommand;
import com.velixo.bitchtalkandroid.command.clientside.AlreadyConnected;
import com.velixo.bitchtalkandroid.command.clientside.ClientSound;
import com.velixo.bitchtalkandroid.command.clientside.Connect;
import com.velixo.bitchtalkandroid.command.clientside.Help;
import com.velixo.bitchtalkandroid.command.clientside.Mute;
import com.velixo.bitchtalkandroid.command.clientside.Unmute;
import com.velixo.bitchtalkandroid.statics.StaticVariables;


public class ClientCommandFactory {
	public final static String HELP = StaticVariables.HELP;
	public final static String MUTE = StaticVariables.MUTE;
	public final static String UNMUTE = StaticVariables.UNMUTE;
	public final static String CONNECT = StaticVariables.CONNECT;
	
	
	private Client client;
	private ClientGui clientGui;
	private Context context;
	
	public ClientCommandFactory(ClientGui cg, Client c, Context cont) {
		clientGui = cg;
		client = c;
		context = cont;
		
	}
	public boolean canBuild(String in){
		return !(build(in) instanceof NotACommand);
	}
	public String help(){
		return "type /connect <ip-address> to connect, bitch.";
	}
	
	public Command build(String input) {
		
		StringTokenizer st = new StringTokenizer(input);
		
		
		switch (st.nextToken()) {
		case HELP:
			return new Help(clientGui, context);
		
		case MUTE:
			return new Mute(clientGui);
			
		case UNMUTE:
			return new Unmute(clientGui);
			
		case CONNECT:
			if (client.connected())
				return new AlreadyConnected(clientGui);
			if(st.hasMoreTokens())
				return new Connect(st.nextToken(),client);
			else
				return new NotACommand(clientGui);
		
		default:
			Log.d("", "build: default. input = " + input);
			if(isServerCommand(input)) {
				Log.d("", "isServerCommand");
				if(isSound(input)) {
					String soundName = input.replace("/:", "") + ".wav";
					return new ClientSound(clientGui, soundName);
				}
			}
			return new NotACommand(clientGui);
		}
	}
	
	private boolean isServerCommand(String input) {
		return (input.charAt(0)=='/' && input.charAt(1)==':');
	}
	
	private boolean isSound(String input) {
		String soundName = input.replace("/:", "");
		try {
			String[] sounds = context.getAssets().list("sounds");
			for (String sound : sounds) {
				if(sound.equals(soundName + ".wav"))
					return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
