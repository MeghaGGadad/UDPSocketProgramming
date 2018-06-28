package org.hvl.CoAPServer;

import org.hvl.CoAP.CoapMessage;
import org.hvl.CoAP.MediaTypeRegistery;
import org.hvl.CoAP.CoAPCodeRegistries;
//import org.hvl.CoAPClient.BasicCoapChannelManager;
import org.hvl.CoAPClient.CoapChannelManager;
import org.hvl.CoAPClient.Request;

public class BasicServer {
	
	
	    private static final int PORT = 5683;
	    static int counter = 0;

	    public static void main(String[] args) {
	        System.out.println("Start CoAP Server on port " + PORT);
	        BasicServer server = new BasicServer();

	        //CoapChannelManager channelManager = BasicCoapChannelManager.getInstance();
	        //channelManager.createServerListener(server, PORT);
	    }

		
		public BasicServer onAccept(Request request) {
			System.out.println("Accept connection...");
			return this;
		}

		
		public void onRequest(CoapServerChannel channel, Request request) {
			System.out.println("Received message: " + request.toString()+ " URI: " + request.getURI());
			
			//CoapMessage response = channel.createResponse(request,
					//messageType.ResponseCode.CONTENT);
			//response.setContentType(MediaTypeRegistery.PLAIN);
			
			//response.setPayload("payload...".getBytes());
			
			if (request.getObserveOption() != null){
				System.out.println("Client wants to observe this resource.");
			}
			
			//response.setObserveOption(1);
			
			//channel.sendMessage(response);
		}

		
		public void onSeparateResponseFailed(CoapServerChannel channel) {
			System.out.println("Separate response transmission failed.");
			
		}
	}


