package com.velixo.bitchtalkandroid.command.clientside;

import com.velixo.bitchtalkandroid.clientSide.ClientGui;
import com.velixo.bitchtalkandroid.command.Command;


public class Unmute implements Command {
private ClientGui c;
	
	public Unmute(ClientGui c) {
		this.c = c;
	}

	@Override
	public void run() {
		c.setMuteNotificationSound(false);
		c.showMessage("Notification sound unmuted, bitch.");
	}

}
