package SocketProgram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class UDPMulticastClient {

	public UDPMulticastClient() {
		System.out.println("UDP Multicast client started");
	
		try(MulticastSocket multicastSocket = new MulticastSocket(9877))
	      {
	      
	      InetAddress inetAddress = InetAddress.getByName("228.5.6.7");
	      multicastSocket.joinGroup(inetAddress);
	      byte[] data = new byte[256];
	      DatagramPacket packet = new DatagramPacket(data, data.length);
	      while (true) {
	    	  
	          System.out.println("Hi this is Client Waiting for server to send message:");
	          
	          multicastSocket.receive(packet);
	          String message = new String(packet.getData(), 0, packet.getLength());
	          System.out.println("Message from: " + packet.getAddress()
	        		  + " Message: [" + message + "]");
	          
	          
	       }
	      }catch (SocketTimeoutException ex) {
          System.out.println("Timeout error: " + ex.getMessage());
          ex.printStackTrace();
      } catch (IOException ex) {
          System.out.println("Client error: " + ex.getMessage());
          ex.printStackTrace();
      } finally{
    	  //if(multicastSocket != null){
    		//  multicastSocket.close();
    		  
    	  }
    	   
	      System.out.println("UDP Multicast client is terminating");
      }
      
    
	

public static void main(String args[]) throws IOException
{  
   
   new UDPMulticastClient ();
}
   

   
   }    







