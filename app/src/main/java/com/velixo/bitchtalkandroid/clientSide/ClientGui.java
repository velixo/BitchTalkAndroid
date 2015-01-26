package com.velixo.bitchtalkandroid.clientSide;

import java.util.List;

public interface ClientGui {
	public void showMessage(String m);
	public void showSilentMessage(String m);
	public void setMuteNotificationSound(boolean b);
	public boolean getNotificationSoundMuted();
	public void updateUsersWindow(List<String> usersInConvo);
	public void playSound(String soundName);
}
