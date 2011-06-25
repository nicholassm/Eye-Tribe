import java.awt.MouseInfo;
import java.io.*;
import java.net.*;

class UDPServer {
  public static void main(String args[]) throws Exception {
 	  java.awt.Point p = new java.awt.Point();
    int port = 8866;

    DatagramSocket serverSocket = new DatagramSocket(port);
    serverSocket.setBroadcast(true);
    InetAddress IPAddress = InetAddress.getByAddress("localhost", new byte[]{10, 0, 2, 2});

    System.out.println("Sending to IP="+IPAddress);

    byte[] sendData;
    while(true) {
      Thread.currentThread().sleep(100);
      p = MouseInfo.getPointerInfo().getLocation();
      String output = "STREAM_DATA 12345 " + p.x + " " + p.y + " " + 18;
      
      System.out.print(output + "\n");
    	
      sendData = output.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
      serverSocket.send(sendPacket);
    }
  }
}
