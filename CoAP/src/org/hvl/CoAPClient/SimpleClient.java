package org.hvl.CoAPClient;

import static java.lang.System.out;

import java.io.IOException;

import org.hvl.CoAP.CoAPOptionRegistry;
import org.hvl.CoAP.MediaTypeRegistery;
import org.hvl.CoAP.Options;
import org.hvl.CoAP.RemoteResource;
import org.hvl.CoAPServer.Response;





public class SimpleClient {
	
	// resource URI path used for discovery
		//private static final String DISCOVERY_RESOURCE = "/.well-known/core";

		// indices of command line parameters
		private static final int METHOD_INDEX  = 0;
		private static final int URI_INDEX     = 1;
		private static final int PAYLOAD_INDEX = 2;
		
		/*
		 * Main method of this client.
		 */
		public static void main(String[] args) throws IOException {
			// initialize parameters
			String method  = null;
			String uri     = null;
			String payload = null;
			boolean loop   = false;

			// display help if no parameters specified
			if (args.length == 0) {
				printInfo();
				return;
			}

			// input parameters
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
				
			// create request according to specified method 
			if (method == null) {
				System.err.println("Method not specified");
				return;
			}
			Request request = newRequest(method);
			
			
			if (request == null) {
				System.err.println("Unknown method: " + method);
				return;
			}
			
			if (method.equals("OBSERVE")) {
				request.setOption(new Options(60, CoAPOptionRegistry.OBSERVE));
				loop = true;
			}
			
			// set request URI
			if (uri == null) {
				System.err.println("URI not specified");
			}
			/*if (method.equals("DISCOVER") && !uri.endsWith(DISCOVERY_RESOURCE)) {
				uri = uri + DISCOVERY_RESOURCE;
			}
			try {
				request.setURI(new URI(uri));
			} catch (URISyntaxException e) {
				System.err.println("Failed to parse URI: " + e.getMessage());
				return;
			}*/
			
			// set request payload
			request.setPayload(payload);
			out.println("Payload: " +payload );
			out.println("Payload Size: " +request.getPayloadSize());
			out.println("Sending Message Type Such as CON,NON,ACK,RST: Current Message Type is " +request.getType());
			//out.println("Token is:" +request.getToken());
			out.println("Code type:GET(1),PUT(2),POST(3),DELETE(4): Current Method Code is " +request.getCode());
			//out.println("Get timestamp: " +request.getTimestamp());
			//out.println("Sending Message Type: " +request.getOptions());
			//out.println("URI: " +request.getURI());
			out.println("Is confirmable message : " +request.isConfirmable());
			out.println("Is reply : " +request.isReply());
			/*try {
				out.println("Respose Receive : " +request.responseReceive());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			out.println("Is Non confirmable message : " +request.isNonConfirmable());
			
			
			
			
			// enable response queue in order to use blocking I/O
			request.enableResponseQueue(true);
			
			request.execute();

			// loop for receiving multiple responses
			do {
			
				// receive response
				
				System.out.println("Receiving response...");
				Response response = null;
				try {
					response = request.responseReceive();
					
					System.out.println("Response is" + response);
					// check for indirect response
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
					System.out.println("Round Trip Time (ms): " + response.getTime());
				
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
		

		
		/*
		 * Outputs user guide of this program.
		 * 
		 */
		public static void printInfo() {
			System.out.println("Java CoAP Simple Client");
			System.out.println();
			System.out.println("Usage: SimpleClient [-l] METHOD URI [PAYLOAD]");
			System.out.println("  METHOD  : {GET, POST, PUT, DELETE, DISCOVER, OBSERVE}");
			System.out.println("  URI     : The URI to the remote endpoint or resource");
			System.out.println("  PAYLOAD : The data to send with the request");
			System.out.println("Options:");
			System.out.println("  -l      : Wait for multiple responses");
			System.out.println();
			System.out.println("Examples:");
			System.out.println("  SimpleClient DISCOVER coap://localhost");
			System.out.println("  SimpleClient POST coap://someServer.org:61616 my data");
		}

		/*
		 * Instantiates a new request based on a string describing a method.
		 * 
		 * @return A new request object, or null if method not recognized
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
			} else if (method.equals("DISCOVER")){
				return new GETRequest();
			} else if (method.equals("OBSERVE")){
				return new GETRequest();
			} else {
				return null;
			}
		}
}
	
