package com.velixo.bitchtalkandroid.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.velixo.bitchtalkandroid.command.clientside.ClientCommandFactory;
import com.velixo.bitchtalkandroid.command.Command;

public class Client {
	
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private ClientGui gui;
	private Context context;
	private ListenForMessagesThread listenForMessagesThread = new ListenForMessagesThread();
	private ClientCommandFactory factory;
    private String lastServer = "";
	
	public Client(ClientGui g, Context c){
		context = c;	//used when checking if sounds exists
		gui = g;
		factory = new ClientCommandFactory(gui,this, context);
//		gui.showMessage(factory.help());
        (new Thread() {
            public void run() {
                while(!isInterrupted()) {
                    dumpInfo("NO_MESSAGE", "dumpInfoThread"); //Used to make sure an infoDump is made every 5 minutes
                    try {
                        sleep(5*60*1000); //5 minutes
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
	}
	
	//TODO check that this code is correct
	public boolean connected() {
		if (connection != null)
			return !connection.isClosed();
		return false;
	}
	
	public void connect(String ip){
		ConnectTask ct = new ConnectTask(ip, "I'm afraid I can't let you do that, bitch.");
		ct.execute(); //TODO refactor, maybe?
	}
	public void send(String message){
        dumpInfo(message, "Client.send");
		try {
			if (message.charAt(0) == '/' && message.charAt(1) != ':' && factory.canBuild(message)) {
				Command c = factory.build(message);
				c.run();
			} else if(isSound(message)) {
				Log.d("", "isSound, message: " + message);
				sendAsSound(message);
				
			} else if(isAdminSound(message)) {
                Log.d("", "isAdminSound, message: " + message);
                sendAsAdminSound(message);
            } else if(isHiddenSound(message)) {
                message = message.replace("/", "/hidden_");
                Log.d("", "isAdminSound, message: " + message);
                sendAsSound(message);

			} else if(output!=null) {
				Log.d("", "not sound, message: " + message);
				output.writeObject(message);
				output.flush();
			} else{
				gui.showMessage("You are not connected to any server.");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private boolean isSound(String input) {
		String soundName = input.replace("/", "") + ".wav";
//		Log.d("", "isSound: " + input);
		return soundExists(soundName);
	}
	
	private boolean isAdminSound(String input) {
		String soundName = input.replace("/", "admin_") + ".wav";
//		Log.d("", "isAdminSound: " + input);
		return soundExists(soundName);
	}

    private boolean isHiddenSound(String input) {
        String soundName = input.replace("/", "hidden_") + ".wav";
//        Log.d("", "isHiddenSound: " + input);
        return soundExists(soundName);
    }
	
	private boolean soundExists(String soundName) {
//		Log.d("", "soundExists: " + soundName);
		try {
			String[] sounds = context.getAssets().list("sounds");
//			Log.d("", "sounds: " + sounds);
			for (String sound : sounds) {
//				Log.d("", "soundExists: " + sound + " ?= " + soundName);
				if(sound.equals(soundName))
					return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void sendAsAdminSound(String message) throws IOException {
		message = message.replace("/", "/:a:");
		if(output!=null){
			output.writeObject(message);
			output.flush();
			System.out.println("flushed, bitch");
		}
		else{
			gui.showMessage("You are not connected to any server.");
		}
	}
	
	private void sendAsSound(String message) throws IOException {
		message = message.replace("/", "/:s:");
		if(output!=null){
			output.writeObject(message);
			output.flush();
			System.out.println("flushed, bitch");
		}
		else{
			gui.showMessage("You are not connected to any server.");
		}
	}

	private void closeCrapAndReconnect(){
        Log.d("", "In Client.closeCrapAndReconnect()");
        gui.showMessage("Reconnecting... bitch.");
        listenForMessagesThread.stopThread();
        try{
            output.close();
            input.close();
            connection.close();
            output = null;
            input = null;
            connection = null;
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        new AttemptConnectionThread(5, lastServer).start();
//        connect(lastServer);
	}

    public void closeCrap() {
        Log.d("", "In Client.closeCrap()");
        gui.showMessage("bitch, I'm out.");
        listenForMessagesThread.stopThread();
        try{
            output.close();
            input.close();
            connection.close();
            output = null;
            input = null;
            connection = null;
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


    private class ListenForMessagesThread extends Thread {
		private boolean runThread;
		public ListenForMessagesThread() {
			runThread = true;
		}

        @Override
		public void run() {
			while(runThread) {
				try {
					//TODO refactor this code. assuming the object is a String or a List<String> is iffy design.
					Object received = input.readObject();
					if (received instanceof String) {
						String message = (String) received;
						Log.d("", "message recieved: " + message);
                        dumpInfo(message, "Client.ListenForMessagesThread");
						if (message.charAt(0) == '/') {
							Command c = factory.build(message);
							c.run();
						} else {
							gui.showMessage(message);
						}
					} else if (received instanceof List){
						@SuppressWarnings("unchecked")
						List<String> usernames = (List<String>) received;
						gui.updateUsersWindow(usernames);
					}
				} catch (ClassNotFoundException | IOException e) {
					gui.showMessage("Disconnected from server");
                    if(runThread){
                        closeCrapAndReconnect();
                    }
					break;
				}
			}
		}
		
		public void stopThread() {
			runThread = false;
		}
	}

    // TODO transform this into a Thread?
	private class ConnectTask extends AsyncTask {
		private String ip;
        private String errorMessage;
		
		public ConnectTask(String ip, String errorMessage) {
			 this.ip = ip;
            this.errorMessage = errorMessage;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = new Socket(InetAddress.getByName(ip),9513);
				output = new ObjectOutputStream(connection.getOutputStream());
				output.flush();
				input = new ObjectInputStream(connection.getInputStream());
                lastServer = ip;
				if (listenForMessagesThread != null)
					listenForMessagesThread.stopThread();
				listenForMessagesThread = new ListenForMessagesThread();
				listenForMessagesThread.start();
			} catch (IOException e) {
				gui.showMessage(errorMessage);
			}
			return null;
		}

	}

    private class AttemptConnectionThread extends Thread {
        private int maxConnAttempts;
        private String ip;
        /** Time that the thread sleeps between each connection attempt, in milliseconds. */
        private int sleepTime = 5000;

        public AttemptConnectionThread(int maxConnectionAttempts, String ip) {
            maxConnAttempts = maxConnectionAttempts;
            this.ip = ip;
        }

        @Override
        public void run () {
            int connAttempts = 0;
            while(!connected() && connAttempts < maxConnAttempts) {
                dumpInfo("conAttemps == " + connAttempts, "Client.AttemptConnectionThread");
                connAttempts++;
                ConnectTask ct = new ConnectTask(ip, "...");
                ct.execute();
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    Log.d("", "AttemptConnectionThread was interrupted");
                }
            }
            if(!connected())
                gui.showSilentMessage("Bitch can't reconnect, lol.");
        }

    }





    private void dumpInfo(String message, String calledFrom) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = df.format(new Date());
        Log.d("BitchTalk", "Client.dumpInfo(), time is " + time);
        Log.d("BitchTalk", "Client.dumpInfo.message ==" + message);
        Log.d("BitchTalk", "Client.dumpInfo.calledFrom ==" + calledFrom);
        Log.d("BitchTalk", "Client.lastServer ==" + lastServer);
        Log.d("BitchTalk", "Client.connection ==" + connection);
        Log.d("BitchTalk", "Client.input ==" + input);
        Log.d("BitchTalk", "Client.output ==" + output);
        Log.d("BitchTalk", "Client.context ==" + context);
        Log.d("BitchTalk", "Client.gui ==" + gui);
        Log.d("BitchTalk", "Client.listenForMessagesThread.getState ==" + listenForMessagesThread.getState());
        Log.d("BitchTalk", "Client.listenForMessagesThread.isAlive ==" + listenForMessagesThread.isAlive());
        Log.d("BitchTalk", "Client.listenForMessagesThread.isInterrupted ==" + listenForMessagesThread.isInterrupted());
        Log.d("BitchTalk", "Client.factory ==" + factory);
    }
}
