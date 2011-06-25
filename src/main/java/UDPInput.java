package com.eyetribe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class UDPInput{
	private ReceiveMessage receiveMessage = new ReceiveMessage();
	private Thread thread = new Thread(receiveMessage);
	private DatagramSocket socketToServer;
	private byte byteArray[] = new byte[50];
	private String gazeString = "";
	private List<Listener> listeners = new ArrayList<Listener>();
	private int port = 8866;
	private boolean flag = true;
	private String x;
	private String y;
	private String[] gazeData;

	public UDPInput(){
			try {
				socketToServer = new DatagramSocket(port);
				thread.start();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public interface Listener {
	    void handleGazeDataSet(UDPInput sender);
	}
	
	public String getGazeString(){
		return gazeString;
	}
	
	public String getX(){
		return x;
	}
	
	public String getY(){
		return y;
	}
	
	public void sendSignal(){
		for (Listener listener : listeners) {
            listener.handleGazeDataSet(this);
        }
	}
	
	public void disconnect(){
		receiveMessage.isDone();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socketToServer.close();
	}
	
	public class ReceiveMessage implements Runnable{
		private boolean running = true;
		
		public void run() {
			
			try{
				while(running){
	                  
					DatagramPacket receivePacket = new DatagramPacket(byteArray, byteArray.length);
	                socketToServer.receive(receivePacket);
	                gazeString = new String(receivePacket.getData());
	                gazeData = gazeString.split(" ");
	                x = gazeData[2].replace(",", "."); 
	                y = gazeData[3].replace(",", ".");
	                sendSignal();
				}
			} 
      catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public void isDone(){
			this.running = false;
		}
	}
	
	  public void addListener(Listener listener) {
		 this.listeners.add(listener);
	  }
		 
	  public void removeListener(Listener listener) {
		 this.listeners.remove(listener);
	  }
}
