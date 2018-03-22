package SocketProgram;
import java.io.*;
import java.net.*;
import java.util.Scanner;

class UDPClient
{
	public UDPClient() {
		System.out.println("UDP client started");
		Scanner scanner = new Scanner(System.in);
	      
		
	      
	      try(DatagramSocket clientSocket = new DatagramSocket())
	      {
	      
	      InetAddress IPAddress = InetAddress.getByName("localhost");
	      while (true) {
	    	  byte[] sendData = new byte[1024];
	          byte[] receiveData = new byte[1024];
	         
	          System.out.println("Waiting for Client Input:");
	          
	          
	          String sentence = scanner.nextLine();
	          if("quit".equalsIgnoreCase(sentence)){
	        	  break;
	        	  
	          }
	         // to send data
	          sendData = sentence.getBytes();
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9003);
	          clientSocket.send(sendPacket);
	          //to receive response
	          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	          
	          clientSocket.receive(receivePacket);
	          String modifiedSentence = new String(receivePacket.getData());
	         
	          
	          System.out.println("FROM SERVER: \n" + modifiedSentence);
	          //clientSocket.close();
	          //Get method added
	          
	          
	      } 
	
      }catch (SocketTimeoutException ex) {
          System.out.println("Timeout error: " + ex.getMessage());
          ex.printStackTrace();
      } catch (IOException ex) {
          System.out.println("Client error: " + ex.getMessage());
          ex.printStackTrace();
      } finally{
    	  //if(clientSocket != null){
    		//clientSocket.close();
    		  if(scanner != null)
    		        scanner.close();
    	  }
    	   
	      System.out.println("UDP client is terminating");
      }
      
    
	

public static void main(String args[]) throws IOException
{  
   
   new UDPClient();
}
   

   
   }    





