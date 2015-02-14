package com.velixo.bitchtalkandroid.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;

import com.velixo.bitchtalkandroid.command.clientside.ClientCommandFactory;
import shared.command.Command;

public class Client {
	
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;

    private HashMap<String,String> macros;
	private ClientGui gui;
	private Context context;
	private ListenForMessagesThread listenForMessagesThread = new ListenForMessagesThread();
    private String lastServer = "";
    private Client me = this;
	
	public Client(ClientGui g, Context c){
		context = c;	//used when checking if sounds exists
		gui = g;
        macros = new HashMap<String,String>();
//		gui.showMessage(factory.help());
        (new Thread() {
            public void run() {
                while(!isInterrupted()) {
//                    dumpInfo("NO_MESSAGE", "dumpInfoThread"); //Used to make sure an infoDump is made every 5 minutes
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
        return connection != null && !connection.isClosed();
    }
	public HashMap<String,String> macroMap(){
        return macros;
    }
	public void connect(String ip){
        if (!connected())  {
            lastServer = ip;
            new ConnectThread(ip, "I'm afraid I can't let you do that, bitch.").start();
//            ConnectTask ct = new ConnectTask(ip, "I'm afraid I can't let you do that, bitch.");
//            ct.execute(); //TODO refactor, maybe?
        } else {
            gui.showSilentMessage("Bitch, you're already connected. Get a fucking grip.");
        }
	}

    public void buildAndRunCommand(String input) {
        if(macros.containsKey(input)){
            input = macros.get(input);
        }
        StringTokenizer st = new StringTokenizer(input,"+");
        while(st.hasMoreTokens()){
            String t = st.nextToken().trim();
            Log.d("","Client.buildAndRun t = " + t);
            Command c = ClientCommandFactory.build(t, context);
            c.clientRun(this);
        }
    }

    public void send(Command c){
        try {
            if(output!=null){
                output.writeObject(c);
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientGui getGui() {
        return gui;
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
                    Command c = (Command) input.readObject();
                    c.clientRunRecieved(me);
				} catch (ClassNotFoundException | IOException e) {
					gui.showMessage("Disconnected from server");
                    if (e instanceof ClassNotFoundException)
                        gui.showSilentMessage("ListenForMessagesThread.CLASSNOTFOUNDEXCEPTION");
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

    private class ConnectThread extends Thread {
        private String ip;
        private String errorMessage;

        public ConnectThread(String ip, String errorMessage) {
            this.ip = ip;
            this.errorMessage = errorMessage;
        }

        @Override
        public void run() {
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
//                dumpInfo("öööh venne", "Client.ConnectThread");
            } catch (IOException e) {
                gui.showMessage(errorMessage);
            }
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
//                dumpInfo("conAttemps == " + connAttempts, "Client.AttemptConnectionThread");
                connAttempts++;
                new ConnectThread(ip, "...").start();
//                ConnectTask ct = new ConnectTask(ip, "...");
//                ct.execute();
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
}
