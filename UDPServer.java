package SocketProgram;


import java.io.IOException;
//import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
class UDPServer
{
   public UDPServer(){
	   System.out.println("UDP server started");
   }
	public static void main(String args[]) throws Exception
      {
		
		DatagramSocket serverSocket = new DatagramSocket(9003);
		
		try{
		   
	   
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            
            
            System.out.println("Server started");
            while(true)
               {
                 
            	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            	
            	serverSocket.receive(receivePacket);
                //modified
            	String sentence = new String( receivePacket.getData(),receivePacket.getOffset(), receivePacket.getLength()); 
                
                System.out.println("RECEIVED: " + sentence);
                
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, IPAddress, port);
                System.out.println("Server is running on port "+ port);
                serverSocket.send(sendPacket);
                
               }
	   }
            catch(SocketException e){
            	System.out.println("Socket: " + e.getMessage());
            }
            catch(IOException e){
            	System.out.println("IO: " + e.getMessage());
            }
	        finally{
	        	if(serverSocket != null)
	        		serverSocket.close();
	        }
	   }
            
      }

      
	
    
      
      
    

