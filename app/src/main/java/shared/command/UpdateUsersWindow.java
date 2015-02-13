package shared.command;

import com.velixo.bitchtalkandroid.clientSide.Client;
import java.util.List;

public class UpdateUsersWindow implements Command {
	private static final long serialVersionUID = -3358150254397660668L;
	private List<String> usernames;

	public UpdateUsersWindow(List<String> usernames) {
		this.usernames = usernames;
	}

	@Override
	public void clientRun(Client c) {

	}

	@Override
	public void clientRunRecieved(Client c) {
		c.getGui().updateUsersWindow(usernames);
	}

}
