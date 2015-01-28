package com.velixo.bitchtalkandroid.fragments;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;
import android.widget.TextView;

import com.velixo.bitchtalkandroid.R;
import com.velixo.bitchtalkandroid.clientSide.Client;
import com.velixo.bitchtalkandroid.clientSide.ClientGui;

import java.io.IOException;
import java.util.List;

public class ChatFragment extends Fragment implements ClientGui {
    private Client client;
    private ScrollView chatScroll;
    private TextView chatWindow;
    private TextView chatInput;

    private boolean notificationMuted = false;

    private static final String NOTIFICATION = "other_notificationsound.wav";

    public ChatFragment() {

    }


    public void addClient(Client client) {
        this.client = client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        chatScroll = (ScrollView) rootView.findViewById(R.id.chatScroll);
        chatWindow = (TextView) rootView.findViewById(R.id.chatWindow);
        chatInput = (TextView) rootView.findViewById(R.id.chatInput);
        showMessage("type /connect <ip-address> to connect, bitch.");
        setChatInputActionListener();
        return rootView;
    }

    @Override
    public void showMessage(final String m) {
        chatWindow.post(new Runnable() {

            @Override
            public void run() {
                Log.d("", "chatFragment.isVisible = " + isVisible());
                Log.d("", "chatFragment.isHidden = " + isHidden());
                boolean atBottom = getChatScrollAtBottom();
                chatWindow.append(m + "\n");
                if (atBottom)
                    chatScroll.scrollTo(0, chatWindow.getHeight());
                if(!notificationMuted)
                    playSound(NOTIFICATION);
            }
        });
    }

    @Override
    public void showSilentMessage(final String m) {
        chatWindow.post(new Runnable() {

			@Override
			public void run() {
                Log.d("", "chatFragment.isVisible = " + isVisible());
                Log.d("", "chatFragment.isHidden = " + isHidden());
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
        //TODO implement
    }

    @Override
    public void playSound(String soundFileName) {
        Log.d("", "playSound: " + soundFileName);
        try {
            AssetFileDescriptor afd;
            afd = getActivity().getAssets().openFd("sounds/" + soundFileName);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
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
                    if (!message.equals("")) {
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
        return diff <= margin;
    }

}