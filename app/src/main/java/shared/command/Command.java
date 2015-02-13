package shared.command;

import java.io.Serializable;

import com.velixo.bitchtalkandroid.clientSide.Client;

public interface Command extends Serializable {
    public void clientRun(Client c);
    public void clientRunRecieved(Client c);
}
