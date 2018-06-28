package org.hvl.CoAPServer;

import java.net.SocketException;
import java.net.*;
import java.io.*;

import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAP.CoAPOptionRegistry;
import org.hvl.CoAP.Options;
import org.hvl.CoAPClient.DELETERequest;
import org.hvl.CoAPClient.GETRequest;
import org.hvl.CoAPClient.POSTRequest;
import org.hvl.CoAPClient.PUTRequest;
import org.hvl.CoAPClient.Request;




public class SimpleServer  {

	

	
		
		public void performGET(GETRequest request) {

			// create response
			Response response = new Response(CoAPCodeRegistries.RESP_CONTENT);
			
			// set payload
			response.setPayload("Hello World!");
			
			// complete the request
			request.respond(response);
		}
	
	
	
		
		public void performPOST(POSTRequest request) {
			
			// retrieve text to convert from payload
			String text = request.getPayloadString();
			
			// complete the request
			request.respond(CoAPCodeRegistries.V3_RESP_OK, text.toUpperCase());
		}
	

	
		
		
		public void performPUT(PUTRequest request) {

			// store payload
			storeData(request);
			
			// complete the request
			request.respond(CoAPCodeRegistries.RESP_CHANGED);
		}		
		
		public void performDELETE(DELETERequest request) {
			
			// disallow to remove the root "storage" resource
			if (!isRoot) {
				
				// remove this resource
				//remove();
				
				request.respond(CoAPCodeRegistries.RESP_DELETED);
			} else {
				request.respond(CoAPCodeRegistries.RESP_FORBIDDEN);
			}
		}
		
		
		
		private void storeData(Request request) {
			
			// set payload and content type
			data = request.getPayload();
			contentType = request.getFirstOption(CoAPOptionRegistry.CONTENT_TYPE);
			
			// signal that resource state changed
			//changed();
		}
		
		private byte[] data;
		private Options contentType;
		private boolean isRoot;
	

	

	public void handleRequest(Request request) {
		
		// output the request
		System.out.println("Incoming request:");
		request.Printlog();
		
		//handle the request
		//super.handleRequest(request);
	}

	
	
	
	public static void main(String[] args) throws SocketException {
		
		SimpleServer server = new SimpleServer();
		
		Request request = new Request(null);
		
		final int port = 5683;
		System.out.printf("SimpleServer listening at port %d.\n", +port);
		
		

		
		
	}

}

