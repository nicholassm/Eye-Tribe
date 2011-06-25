import java.awt.MouseInfo;
import java.io.*;
import java.net.*;

class UDPServer
{
   public static void main(String args[]) throws Exception
      {
 		java.awt.Point p = new java.awt.Point();
        int port = 8866;

         DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] sendData = new byte[1024];
            while(true)
               {
    			  p = MouseInfo.getPointerInfo().getLocation();
    			  String output = "STREAM_DATA 12345 " + p.x + " " + p.y + " " + 18;
    			  
    			  System.out.print(output + "\n");
            	
                  InetAddress IPAddress = InetAddress.getByName("localhost");
                  sendData = output.getBytes();
                  DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
               }
      }
}
