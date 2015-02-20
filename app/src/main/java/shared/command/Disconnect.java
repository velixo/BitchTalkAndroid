package shared.command;

import com.velixo.bitchtalkandroid.clientSide.Client;

public class Disconnect implements Command {
    private static final long serialVersionUID = -6073388303812926805L;

    @Override
    public void clientRun(Client c) {
        c.closeCrap();
    }

    @Override
    public void clientRunRecieved(Client c) {
        c.closeCrap();
    }

}
