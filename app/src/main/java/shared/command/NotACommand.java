package shared.command;

import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.statics.StaticVariables;

public class NotACommand implements Command {
    private static final long serialVersionUID = 1L;
    private final String NACMSG = StaticVariables.NOTACOMMANDMESSAGE;

    @Override
    public void clientRun(Client c) {
        c.getGui().showMessage(NACMSG);
    }

    @Override
    public void clientRunRecieved(Client c) {
    }
}
