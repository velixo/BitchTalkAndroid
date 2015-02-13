package com.velixo.bitchtalkandroid.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.velixo.bitchtalkandroid.command.clientside.ClientCommandFactory;
import shared.command.Command;

public class Client {
	
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private ClientGui gui;
	private Context context;
	private ListenForMessagesThread listenForMessagesThread = new ListenForMessagesThread();
	private ClientCommandFactory factory;
    private String lastServer = "";
    private Client me = this;
	
	public Client(ClientGui g, Context c){
		context = c;	//used when checking if sounds exists
		gui = g;
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
		if (connection != null)
			return !connection.isClosed();
		return false;
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
        Command c = ClientCommandFactory.build(input, context);
        c.clientRun(this);
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
