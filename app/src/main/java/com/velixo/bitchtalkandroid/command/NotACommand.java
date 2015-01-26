package com.velixo.bitchtalkandroid.command;

import com.velixo.bitchtalkandroid.clientSide.ClientGui;

//import serverside.ServerGui;
//import serverside.User;

public class NotACommand implements Command {
//	private ServerGui s;
	private ClientGui c;
//	private User u;
	
//	public NotACommand(ServerGui s) {
//		this.s = s;
//	}
	
	public NotACommand(ClientGui c) {
		this.c = c;
	}
	
//	public NotACommand(User u) {
//		this.u = u;
//	}

	@Override
	public void run() {
		//TODO: snygga till.
//		if (s != null) {
//			s.showMessage("Invalid command!");
//		}
		if (c != null) {
			c.showMessage("Invalid command!");
		}
//		if (u != null) {
//			try {
//				u.send("Invalid command!");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

}
