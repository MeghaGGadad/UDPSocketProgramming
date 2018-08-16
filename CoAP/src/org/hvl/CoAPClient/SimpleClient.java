package org.hvl.CoAPClient;

import static java.lang.System.out;

import java.io.IOException;

import org.hvl.CoAP.MediaTypeRegistery;
import org.hvl.CoAP.RemoteResource;
import org.hvl.CoAPServer.Response;

public class SimpleClient {
	
	    // indices of command line parameters
		private static final int METHOD_INDEX  = 0;
		private static final int URI_INDEX     = 1;
		private static final int PAYLOAD_INDEX = 2;
		
		/*
		 * Main method of this SIMPLE client.
		 * This method check for missing arguments
		 */
		public static void main(String[] args) throws IOException, InterruptedException {
			
			String method  = null;
			String uri     = null;
			String payload = null;
			boolean loop   = false;

			// display information  if no arguments specified
			if (args.length == 0) {
				printInfo();
				return;
			}

			
			int index = 0;
			for (String arg : args) {
				if (arg.startsWith("-")) {
					if (arg.equals("-l")) {
						loop = true;
					} else {
						System.out.println("Unrecognized option: " + arg);
					}
				} else {
					switch (index) {
					case METHOD_INDEX:	
						method = arg.toUpperCase();
						break;
					case URI_INDEX:	
						uri = arg;
						break;
					case PAYLOAD_INDEX: 
						payload = arg;
						break;
					default:
						System.out.println("Unexpected argument: " + arg);
					}
					++index;
				}
			}
				
			// create request according to parameter passed 
			if (method == null) {
				System.err.println("Method not specified");
				return;
			}
			Request request = newRequest(method);
			if (request == null) {
				System.err.println("Unknown method: " + method);
				return;
			}
			// set request URI
			if (uri == null) {
				System.err.println("URI not specified");
			}
		// set request payload
		   request.setPayload(payload);
		   out.println("Client is running: ");
		   out.println("Payload: " +payload );
		   out.println("Payload Size: " +request.getpayloadSize());
		   out.println("Current Message Type is " +request.getType());
	       out.println("Current Method Code is " +request.getCode());
	       out.println("Is confirmable message : " +request.isConfirmable());
		   out.println("Is reply : " +request.isReply());
		   out.println("Is Non confirmable message : " +request.isNonConfirmable());
		   out.println("Current version is : " +request.getVersion());
		   int id = 1234;
		   out.println("Current MessageId is : " +request.setID(id));
			
		   // enable response queue in order to use blocking I/O
			request.ResponseQueueEnable(true);
			
			request.execute();

			// loop for receiving multiple responses
			do {
			
				// receive response
				
				System.out.println("Receiving response from Server...");
				Response response = null;
				
				try {
					
					response = request.responseReceive();
					
					
					System.out.println("Response is" + response);
					// check for if response is not null and it has empty ACK
					if (response != null && response.isEmptyACK()) {
						response.Printlog();
						System.out.println("Request acknowledged, waiting for separate response...");
						
						response = request.responseReceive();
					}
					
				} catch (InterruptedException e) {
					System.err.println("Failed to receive response: " + e.getMessage());
					return;
				}
		
				// output response
				
				if (response != null) {
					
					response.Printlog();
					System.out.println("Total Time (ms): " + response.getTime());
				
					// check of response contains resources
					if (response.hasFormat(MediaTypeRegistery.LINK_FORMAT)) {
						
						String linkFormat = response.getPayloadString();
						
						// create resource three from link format
						RemoteResource root = RemoteResource.newRoot(linkFormat);
						if (root != null) {
							
							// output discovered resources
							System.out.println("\nDiscovered resources:");
							root.log();
							
						} else {
							System.err.println("Failed to parse link format");
						}
					} else {
						
						// check if link format was expected by client
						if (method.equals("DISCOVER")) {
							System.out.println("Server error: Link format not specified");
						}
					}
					
				} else {
					
					// no response received
					// calculate time elapsed 
					long elapsed = System.currentTimeMillis() - request.getTimestamp();
					
					System.out.println("Request timed out (ms): " + elapsed);
					break;
				}
				
			} while (loop);
			
			// finish
			System.out.println();
		}
		/* If the user is not aware of what the arguments should pass
		 * 
		 * 
		 */
		public static void printInfo() {
			System.out.println("Java CoAP Simple Client");
			System.out.println();
			System.out.println("Usage: SimpleClient [-l] METHOD URI [PAYLOAD]");
			System.out.println("  METHOD  : {GET, POST, PUT, DELETE}");
			System.out.println("  URI     : The URI to the remote endpoint or resource");
			System.out.println("  PAYLOAD : The data to send with the request");
			System.out.println("Options:");
			System.out.println("  -l      : Wait for multiple responses");
			System.out.println();
			System.out.println("Examples:");
			System.out.println("  SimpleClient GET coap://10.0.1.97:5683/temp");
			System.out.println("  SimpleClient POST coap://example.com:5683/~sensors/readings.xml");
		}

		/* This method 
		 * instantiates a new request based on a method.
		 * 
		 * @return A new request object depending on method passed, or null if method not correct
		 */
		private static Request newRequest(String method) {
			if (method.equals("GET")) {
				return new GETRequest();
			} else if (method.equals("POST")) {
				return new POSTRequest();
			} else if (method.equals("PUT")) {
				return new PUTRequest();
			} else if (method.equals("DELETE")) {
				return new DELETERequest();
			} else {
				return null;
			}
		}

		

		

		}		

		
	
		
	
