package org.hvl.CoAPClient;



import java.net.URI;


import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAPServer.Response;

public class Coapclient1 {

	/** The type used for requests (CON is default) */
	private CoAPCodeRegistries.Type type = CoAPCodeRegistries.Type.CON;
	
	/** The destination URI */
	private String uri;
	
	/** The timeout. */
	private long timeout;


   /**
   * Constructs a new CoapClient that has no destination URI yet.
   */
   public Coapclient1() {
	this("");
   }
   
   /**
	 * Constructs a new CoapClient that sends requests to the specified URI.
	 *
	 * @param uri the uri
	 */
	public Coapclient1(String uri) {
		this.uri = uri;
	}
	
	/**
	 * Constructs a new CoapClient that sends request to the specified URI.
	 * 
	 * @param uri the uri
	 */
	public Coapclient1(URI uri) {
		this(uri.toString());
	}
	
	/**
	 * Constructs a new CoapClient with the specified scheme, host, port and 
	 * path as URI.
	 *
	 * @param scheme the scheme
	 * @param host the host
	 * @param port the port
	 * @param path the path
	 */
	public Coapclient1(String scheme, String host, int port, String... path) {
		StringBuilder builder = new StringBuilder()
			.append(scheme).append("://").append(host).append(":").append(port);
		for (String element:path)
			builder.append("/").append(element);
		this.uri = builder.toString();
	}
	
	/**
	 * Gets the timeout.
	 *
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}
	
	/**
	 * Sets the timeout how long synchronous method calls will wait until they
	 * give up and return anyways. The value 0 is equal to infinity.
	 *
	 * @param timeout the timeout
	 * @return the CoAP client
	 */
	public Coapclient1 setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}
	
	/**
	 * Gets the destination URI of this client.
	 *
	 * @return the uri
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * Sets the destination URI of this client.
	 *
	 * @param uri the uri
	 * @return the CoAP client
	 */
	public Coapclient1 setURI(String uri) {
		this.uri = uri;
		return this;
	}
	
	/**
	 * Let the client use Confirmable requests
	 * 
	 * @return the CoAP client
	 */
	public Coapclient1 useCONs() {
		this.type = CoAPCodeRegistries.Type.CON;
		return this;
	}
	
	/**
	 * Let the client use Non-Confirmable requests.
	 * 
	 * @return the CoAP client
	 */
	public Coapclient1 useNONs() {
		this.type = CoAPCodeRegistries.Type.NON;
		return this;
	}
	
	/**
	 * Performs a CoAP ping using the default timeout for requests.
	 * 
	 * @return success of the ping
	 
	public boolean ping() {
		return ping(this.timeout);
	}*/

	/**
	 * Performs a CoAP ping and gives up after the given number of milliseconds.
	 * 
	 * @param timeout the time to wait for a pong in ms
	 * @return success of the ping
	 
	public boolean ping(long timeout) {
		try {
			Request request = new Request(null, messageType.Type.CON);
			request.setToken(new byte[0]);
			request.setURI(uri);
			request.send().waitForResponse(timeout);
			return request.isRejected();
		} catch (InterruptedException e) {
			// waiting was interrupted, which is fine
		}
		return false;
	}
	*/
	// Synchronous GET
	
		/**
		 * Sends a GET request and blocks until the response is available.
		 * 
		 * @return the CoAP response
		 */
		public Response get() {
			return synchronous(Request.newGet().setURI(uri));
		}

		private Response synchronous(boolean setURI) {
			// TODO Auto-generated method stub
			return null;
		}
}   