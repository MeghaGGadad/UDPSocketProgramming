package org.hvl.CoAPServer;

import java.net.SocketException;

import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAPClient.GETRequest;
import org.hvl.CoAPClient.Request;






public class SimpleServer {
	
public void handleRequest(Request request) {
		
		// output the request
		System.out.println("Incoming request:");
		request.Printlog();
		
		//handle the request
		//super.handleRequest(request);
	}

public SimpleServer() throws SocketException {
		
		// add resources to the server
		addResource(new HelloWorldResource());
	
}

private void addResource(HelloWorldResource helloWorldResource) {
	// TODO Auto-generated method stub
	
}

private class HelloWorldResource extends GETRequest {
	
	private String resourceName;

	public HelloWorldResource() {
		
		setResourceName("GET a friendly greeting!");
	}
	
	private void setResourceName(String resourceName) {
		this.resourceName = resourceName; 
		
		
	}

	@Override
	public void performGET(GETRequest request) {

		// create response
		Response response = new Response(CoAPCodeRegistries.RESP_CONTENT);
		
		// set payload
		response.setPayload("Hello World!");
		
		// complete the request
		request.respondback(response);
	}
}

public static void main(String[] args) throws SocketException {
	
	SimpleServer server = new SimpleServer();
	
	
	
	final int port = 5683;
	System.out.printf("SimpleServer listening at port %d.\n", +port);
	
	Response response = new Response(CoAPCodeRegistries.RESP_CONTENT);
	//GETRequest request = null;
	//request.respond(response);
}

}

