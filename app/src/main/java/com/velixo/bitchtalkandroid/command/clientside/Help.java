package com.velixo.bitchtalkandroid.command.clientside;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.velixo.bitchtalkandroid.clientSide.ClientGui;
import com.velixo.bitchtalkandroid.command.Command;
import com.velixo.bitchtalkandroid.statics.StaticVariables;

public class Help implements Command {
	private ClientGui gui;
	private Context context;
	
	public Help(ClientGui gui, Context context) {
		this.gui = gui;
		this.context = context;
	}

	@Override
	public void run() {
		String help = "\nBitch needed some commands?\n" +
				StaticVariables.HELP + "\n" +
				StaticVariables.CONNECT + "\n" +
				StaticVariables.MUTE + "\n" +
				StaticVariables.UNMUTE + "\n" +
				StaticVariables.BITCHLIST + "\n" + 
				StaticVariables.SETNAME + "\n";
		List<String> sounds = getNormalSoundNames();
		for(String sound : sounds) {
			help += "/" + sound + "\n";
		}
		gui.showSilentMessage(help);
	}
	
	private List<String> getNormalSoundNames() {
		try {
			List<String> soundList = new ArrayList<String>();
			String[] sounds = context.getAssets().list("sounds");
			for (String sound : sounds) {
				if (!sound.contains("other_") && !sound.contains("admin_") && !sound.contains(".raw")) { //for some reason there are two .raw files in assets. weird.
					soundList.add(sound.replace(".wav", ""));
				}
			}
			return soundList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
