package dummy;
import java.net.*;



import java.io.*;

public class CoAP_client {
	
          public static final int DEFAULT_PORT      = 5683;
	      public static final String URI_SCHEME_NAME = "coap";
	      byte[] buffer = new byte[1024];
	      DatagramSocket socket  = new DatagramSocket();
	      private ReceiverThread receiverThread;	
	      
	      class ReceiverThread extends Thread{
	    	  @Override
	  		public void run() {
	  			// always listen for incoming datagrams
	  			while (true) {
	  				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
	  			    
	  				try {
						socket.receive(incoming);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
	  				
	  				datagramReceived(incoming);
	  			 }
	    
	    	  }
	    	  
	      }
	      
	      public CoAP_client(int port, boolean daemon) throws SocketException
	    			
	    		{
	    			// initialize members
	    			this.socket = new DatagramSocket(port);
	    			this.receiverThread = new ReceiverThread();

	    			// decide if receiver thread terminates with main thread
	    			receiverThread.setDaemon(daemon);
	    			
	    			// start listening right from the beginning
	    			this.receiverThread.start();
	    			
	    		}
	      
	      public CoAP_client() throws SocketException
	    		{
	    			this(0, true); // use any available port on the local host machine
	    			// TODO use -1 instead of 0?
	    		}
	      
	      public void setDaemon(boolean on) {
	  		receiverThread.setDaemon(on);
	  	}
	    	
	      public boolean isDaemon() {
	  		return receiverThread.isDaemon();
	  	}
	  	
	  	public int getPort() {
	  		return socket.getLocalPort();
	  	}	
        
	  	
	  	//@Override
		protected void doSendMessage(MessageFormat msg) throws IOException {
			
			// assemble datagram components:
			
			// retrieve URI
			URI uri = msg.getURI();
			
			// retrieve remote address
			// throws UnknownHostException, subclass of IOException
			InetAddress address = msg.getAddress();
			
			// retrieve remote port
			int port = uri != null ? uri.getPort() : -1;
			if (port < 0) port = DEFAULT_PORT;

			// retrieve payload
			byte[] payload = msg.toByteArray();
			
			// create datagram		
			DatagramPacket datagram = 
				new DatagramPacket(payload, payload.length, address, port);
			
			// remember when this message was sent
			msg.setTimestamp(System.currentTimeMillis());
			
			// send it over the UDP socket
			socket.send(datagram);
		}
	  	
	}
	
           
	
	

