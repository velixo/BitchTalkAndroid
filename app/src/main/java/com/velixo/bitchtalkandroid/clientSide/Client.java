package com.velixo.bitchtalkandroid.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.velixo.bitchtalkandroid.command.ClientCommandFactory;
import com.velixo.bitchtalkandroid.command.Command;

public class Client {
	
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private ClientGui gui;
	private Context context;
	private ListenForMessagesThread listenForMessagesThread = new ListenForMessagesThread();
	private ClientCommandFactory factory;
	
	public Client(ClientGui g, Context c){
		context = c;	//used when checking if sounds exists
		gui = g;
		factory = new ClientCommandFactory(gui,this, context);
		gui.showMessage(factory.help());
	}
	
	//TODO check that this code is correct
	public boolean connected() {
		if (connection != null)
			return !connection.isClosed();
		return false;
	}
	
	public void connect(String ip){
		ConnectTask ct = new ConnectTask(ip);
		ct.execute(); //TODO refactor, maybe?
	}
	public void send(String message){
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
		Log.d("", "isSound: " + input);
		return soundExists(soundName);
	}
	
	private boolean isAdminSound(String input) {
		String soundName = input.replace("/", "admin_") + ".wav";
		Log.d("", "isAdminSound: " + input);
		return soundExists(soundName);
	}
	
	private boolean soundExists(String soundName) {
		Log.d("", "soundExists: " + soundName);
		try {
			String[] sounds = context.getAssets().list("sounds");
			Log.d("", "sounds: " + sounds);
			for (String sound : sounds) {
				Log.d("", "soundExists: " + sound + " ?= " + soundName);
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
	
	
	
	
	private void closeCrap(){
		gui.showMessage("bitch, I'm out.");
		try{
			output.close();
			input.close();
			connection.close();
         }catch(IOException ioe){
        	ioe.printStackTrace();
         }
	}
	
	
	private class ListenForMessagesThread extends Thread {
		private boolean runThread;
		public ListenForMessagesThread() {
			runThread = true;
		}
		
		public void run() {
			//TODO disconnect functionality
			while(runThread) {
				try {
					//TODO refactor this code. assuming the object is a String or a List<String> is iffy design.
					Object received = input.readObject();
					if (received instanceof String) {
						String message = (String) received;
						Log.d("", "message recieved: " + message);
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
					closeCrap();
					break;
				}
			}
		}
		
		public void stopThread() {
			runThread = false;
		}
	}
	
	private class ConnectTask extends AsyncTask {
		private String ip;
		
		public ConnectTask(String ip) {
			 this.ip = ip;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = new Socket(InetAddress.getByName(ip),9513);
				output = new ObjectOutputStream(connection.getOutputStream());
				output.flush();
				input = new ObjectInputStream(connection.getInputStream());
				if (listenForMessagesThread != null)
					listenForMessagesThread.stopThread();
				listenForMessagesThread = new ListenForMessagesThread();
				listenForMessagesThread.start();
			} catch (IOException e) {
				gui.showMessage("I'm afraid I can't let you do that, bitch.");
			}
			return null;
		}

	}
	
}
