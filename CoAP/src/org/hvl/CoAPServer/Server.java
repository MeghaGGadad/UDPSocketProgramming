package org.hvl.CoAPServer;

import java.net.SocketException;

import org.hvl.CoAP.CoapResource;

public class Server {
	
	
	

	

	
	    
	    /*
	     * Application entry point.
	     */
	   public static void main(String[] args) {
	        
	        try {
	            
	            // create server
	            Server server = new Server();
	            server.start();
	            
	        } catch (SocketException e) {
	            
	            System.err.println("Failed to initialize server: " + e.getMessage());
	        }
	    }
	    
	    private void start() {
		// TODO Auto-generated method stub
		
	}

	/*
	     * Constructor for a new Hello-World server. Here, the resources
	     * of the server are initialized.
	     */
	  public Server() throws SocketException {
	        
	        // provide an instance of a Hello-World resource
	        add(new HelloWorldResource());
	    }
	    
	    private void add(HelloWorldResource helloWorldResource) {
		// TODO Auto-generated method stub
		
	}

	/*
	     * Definition of the Hello-World Resource
	     */
	  class HelloWorldResource extends CoapResource {
	        
	        public HelloWorldResource() {
	            
	            // set resource identifier
	            super("helloWorld");
	            
	            // set display name
	            //((Object) getAttributes()).setTitle("Hello-World Resource");
	        }
	        
	        private Object getAttributes() {
				// TODO Auto-generated method stub
				return null;
			}
	        
	        

			@Override
	        public void handleGET(CoapExchange exchange) {
	            
	            // respond to the request
	            exchange.respond(null, "Hello World!");
	        }
	    }
	}



