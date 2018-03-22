package SocketProgram;

import java.io.IOException;
import java.net.DatagramPacket;
//import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Date;

public class UDPMulticastServer {
	public UDPMulticastServer(){
		   System.out.println("UDP Multicast Time Server Started");
		   
		   try(MulticastSocket multicastSocket = new MulticastSocket())
			{
				Thread.sleep(1000);
			   
			   InetAddress inetAddress = InetAddress.getByName("228.5.6.7");
			   multicastSocket.joinGroup(inetAddress);
	           
	           //to send message.....
	           byte[] sendData = new byte[1024];
	           DatagramPacket packet;
	           
	           System.out.println("UDP Multicast Server started");
	           while(true)
	              {
	                
	           	   
	           	   String message = (new Date()).toString();
	           	   System.out.println("Sending : [" + message + "]");
	           	   sendData = message.getBytes();
	        	   packet = new DatagramPacket(sendData, message.length(), inetAddress, 9877);
	        	   multicastSocket.send(packet);
	               System.out.println("UDP Multicast Server terminating");
	              }
		   }catch(SocketException e){
	       	System.out.println("Socket: " + e.getMessage());
	       }
	       catch(IOException e){
	       	System.out.println("IO: " + e.getMessage());
	       }
		   catch( InterruptedException e){
			   System.out.println("Socket: " + e.getMessage());   
		   }
	       finally{
	       	
	       }
	   }
		public static void main(String args[]) throws Exception
	      {
			new UDPMulticastServer ();
	      }
	}

