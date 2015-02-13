package shared.command;


import com.velixo.bitchtalkandroid.clientSide.Client;

public class UnrecognizedCommand implements Command {
	private static final long serialVersionUID = -8445019066399527186L;
	private String input;
	
	public UnrecognizedCommand(String input) {
		this.input = input;
	}

	@Override
	public void clientRun(Client c) {
		c.send(this);
	}

	@Override
	public void clientRunRecieved(Client c) {
		
	}

}
