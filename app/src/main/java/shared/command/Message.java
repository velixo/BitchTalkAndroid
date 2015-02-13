package shared.command;

import com.velixo.bitchtalkandroid.clientSide.Client;

public class Message implements Command {
	private static final long serialVersionUID = -8662147529730890787L;
	private String text;
	
	public Message(String s) {
		this.text = s;
	}

	@Override
	public void clientRunRecieved(Client c) {
		c.getGui().showMessage(text);
	}


	@Override
	public void clientRun(Client c) {
		c.send(this);
	}
}
