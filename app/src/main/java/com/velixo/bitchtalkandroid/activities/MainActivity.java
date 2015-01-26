package com.velixo.bitchtalkandroid.activities;


import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;
import android.widget.TextView;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.clientSide.ClientGui;

public class MainActivity extends Activity implements ClientGui {
	private ScrollView chatScroll;
	private TextView chatWindow;
	private TextView chatInput;
	private Client client;
	
	private boolean notificationMuted = false;
	
	private final String NOTIFICATION = "other_notificationsound.wav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatScroll = (ScrollView) findViewById(R.id.chatScroll);
        chatWindow = (TextView) chatScroll.getChildAt(0);
        chatInput = (TextView) findViewById(R.id.chatInput);
        client = new Client(this, this);
        setChatInputActionListener();
    }
    
    
	@Override
	public void showMessage(final String m) {
		chatWindow.post(new Runnable() {

			@Override
			public void run() {
				boolean atBottom = getChatScrollAtBottom();
				chatWindow.append(m + "\n");
				if(atBottom)
					chatScroll.scrollTo(0, chatWindow.getHeight());
				playSound(NOTIFICATION);
			}
		});
	}
	
	@Override
	public void showSilentMessage(final String m) {
		chatWindow.post(new Runnable() {

			@Override
			public void run() {
				boolean atBottom = getChatScrollAtBottom();
				chatWindow.append(m + "\n");
				if (atBottom)
					chatScroll.scrollTo(0, chatWindow.getHeight());
			}
		});
	}

	@Override
	public void setMuteNotificationSound(boolean b) {
		notificationMuted = b;
	}

	@Override
	public boolean getNotificationSoundMuted() {
		return notificationMuted;
	}

	@Override
	public void updateUsersWindow(List<String> usersInConvo) {
		// TODO implement this
		
	}

	@Override
	public void playSound(String soundFileName) {
		Log.d("", "playSound: " + soundFileName);
		try {
			if(!soundFileName.equals(NOTIFICATION) || !notificationMuted) {
				AssetFileDescriptor afd;
				afd = getAssets().openFd("sounds/" + soundFileName);
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				player.prepare();
				player.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			showSilentMessage("Bitch, you aint even got " + soundFileName);
		}
	}
	
	private void setChatInputActionListener() {
		chatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String message = chatInput.getText().toString();
					if(!message.equals("")) {
						client.send(message);
						chatInput.setText("");
					}
					return true;
				}
				return false;
			}
		});
	}
	
	private boolean getChatScrollAtBottom() {
		int margin = 100;
		int diff = chatWindow.getBottom() - (chatScroll.getHeight() + chatScroll.getScrollY());
		if(diff <= margin)
			return true;
		return false;
	}
}
