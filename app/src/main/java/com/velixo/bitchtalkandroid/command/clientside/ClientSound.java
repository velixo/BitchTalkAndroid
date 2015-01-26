package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.ClientGui;
import com.velixo.bitchtalkandroid.command.Command;

public class ClientSound implements Command {
	private ClientGui g;
	private String soundFileName;
	
	public ClientSound(ClientGui g, String soundFileName) {
		this.g = g;
		this.soundFileName = soundFileName;
	}

	@Override
	public void run() {
		g.playSound(soundFileName);
	}
}
