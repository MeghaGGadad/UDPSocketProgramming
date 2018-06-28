package org.hvl.CoAPClient;

import java.net.URI;
import java.net.URISyntaxException;

import org.hvl.CoAP.PrettyPrint;
import org.hvl.CoAPServer.Response;

public class Client {
	
public static void main(String args[]) {
		
		URI uri = null; // URI parameter of the request
		
		System.out.println("waiting for user input: Enter the Valid URI");
		if (args.length > 0) {
			
			// input URI from command line arguments
			try {
				uri = new URI(args[0]);
			} catch (URISyntaxException e) {
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
			Coapclient1 client = new Coapclient1(uri);

			Response response = client.get();
			
			if (response!=null) {
				
				System.out.println(response.getCode());
				//System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				
				System.out.println("\nADVANCED\n");
				// access advanced API with access to more details through .advanced()
				System.out.println(PrettyPrint.prettyPrint(response));
				
			} else {
				System.out.println("No response received.");
			}
			
		} else {
			// display help
			System.out.println("Client");
			System.out.println("(c) 2018, HVL");
			System.out.println();
			System.out.println("Usage: " + Client.class.getSimpleName() + " URI");
			System.out.println("  URI: The CoAP URI of the remote resource to GET");
		}
	}

}


