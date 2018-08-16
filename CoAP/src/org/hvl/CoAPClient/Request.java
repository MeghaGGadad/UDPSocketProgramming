package org.hvl.CoAPClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAP.CoAPCodeRegistries.Code;
import org.hvl.CoAP.CoAPCodeRegistries.Type;
import org.hvl.CoAP.CoAPOptionRegistry;
import org.hvl.CoAP.MessageFormat;
import org.hvl.CoAPServer.HandelResponse;
import org.hvl.CoAPServer.Response;




public class Request extends MessageFormat {
	
	private BlockingQueue<Response> responseQueue;
	private static final Response TIMEOUT_RESPONSE = new Response();
	 
	private int responseCount;
	//public void setUriHost(String host);
	
	Request request;
	// list of response handlers that are notified about incoming responses
	private List<HandelResponse> responseHandlers;
	
	//private static final long startTime = System.currentTimeMillis();
	
	/** To indicate current response for the request. */
	private Response response;
	
	/** This object used to wait for a response. */
	private Object lock;
	
	/** The request code. */
	private CoAPCodeRegistries.Code code;
	
		
	// Constructors 
		
	public Request(Code code) {
		super();
		this.code = code;
	}
		
	/* Constructor for a upcoming new CoAP messages
	 * @param code The method code of the message
	 * @param confirmable True if the request is to be sent as a CON
	 */ 
		public Request(int code, boolean confirmable) {
			//this.code = code;
			super(confirmable ? 
				Type.CON : Type.NON, code);
		}
		
		/* This method places a new response to this request, 
		 * 
		 * @param response A response to the request
		 */
		public void respondback(Response response) {
			
			// assign response to the request
			response.setRequest(this);
			
			response.setURI(getURI());
			response.setOption(getFirstOption(CoAPOptionRegistry.TOKEN));

			
			if (responseCount == 0 && isConfirmable()) {
				response.setID(getID());
			}
			
			// set message type
			if (response.getType() == null) {
				if (responseCount == 0 && isConfirmable()) {
					// uses the piggy-backed response
					response.setType(Type.ACK);
				} else {
					// use separate response depending on the type:
					// CON response to CON request, 
					// NON response to NON request
					response.setType(getType());
				}
			}
			++responseCount;
		}
			
		/**
		 * Wait for the response. This function blocks until there is a response or
		 * the request has been canceled.
		 * 
		 * @return the response
		 * @throws InterruptedException
		 *             the interrupted exception
		 */
		/*public Response waitForResponse() throws InterruptedException {
			return waitForResponse(0);
		}*/	
		
       public void respondback(int code, String message) {
			Response response = new Response(code);
			if (message != null) {
				response.setPayload(message);
			}
			respondback(response);
		}

		public void respondback(int code) {
			respondback(code, null);
		}
		
		public void accept() {
			if (isConfirmable()) {
				Response ack = new Response(CoAPCodeRegistries.EMPTY_MESSAGE);
				ack.setType(CoAPCodeRegistries.Type.ACK);
				respondback(ack);
			}
		}

		public void reject() {
			if (isConfirmable()) {
				Response rst = new Response(CoAPCodeRegistries.EMPTY_MESSAGE);
				rst.setType(Type.RST);
				respondback(rst);
			}
		}
		
		public void responseCompleted(Response response) {
			System.out.println("Completed");
		}
		
		
		/*
		 * This method returns a response placed using respond() and
		 * blocks until a response is available.
		 */
		public Response responseReceive() throws InterruptedException {
			
			// queue needed to perform this operation
			if (!responseQueueOn()) {
				System.out.println("WARNING: Responses may be lost, because of  Missing useResponseQueue(true) call, ");
				ResponseQueueEnable(true);
			}
			
			// receive response from a response queue
			Response response = responseQueue.take();
			
			// return back null if request timed out
			return response != TIMEOUT_RESPONSE ? response : null; 
		}
		
		@Override
		public void timedOut() {
			if (responseQueueOn()) {
				responseQueue.offer(TIMEOUT_RESPONSE);
			}
		}

		/*
		 * This method Registers a handler for responses to this request
		 * 
		 * 
		 */
		public void ResponseHandlerRegister(HandelResponse handler) {

			if (handler != null) {
				
				// creation of response handler list
				if (responseHandlers == null) {
					responseHandlers = new ArrayList<HandelResponse>();
				}
				
				responseHandlers.add(handler);
			}
		}

		/*
		 * Method to Unregisters a handler for responses to this request
		 * 
		 * @param handler The observer to remove from the handler list
		 */	
		public void ResponseHandlerUnregister(HandelResponse handler) {

			if (handler != null && responseHandlers != null) {
				
				responseHandlers.remove(handler);
			}
		}

		/*
		 * Method to Enable or disable the response queue
		 * 
		 *  NOTE: The response queue must be ON/enabled before making  
		 *       calls to responseReceive()
		 * 
		 * @param enable True to make enable and false to disable the response queue,
		 * respectively
		 */
		public void ResponseQueueEnable(boolean enable) {
			if (enable != responseQueueOn()) {
				responseQueue = enable ? new LinkedBlockingQueue<Response>() : null;
			}
		}
		
		/*
		 * To check if the response queue is enabled
		 * 
		 * NOTE: The response queue must be enabled BEFORE any possible
		 *       calls to responseReceive()
		 * 
		 * @return True iff the response queue is enabled, otherwise false
		 */	
		public boolean responseQueueOn() {
		if(responseQueue != null)
		    return true;
		else
			return false;
		}	
		
		/*
		 * This method is called whenever a response was placed to this request.
		 * other classes can override this method in order to handle responses.
		 * 
		 * @param response The response to handle
		 */
		public void responseHandel(Response response) {

			// add the response
			if (responseQueueOn()) {
				if (!responseQueue.offer(response)) {
					System.out.println("ERROR: Failed to add response to request");
				}
			}
		
			// notify response handlers
			if (responseHandlers != null) {
				for ( HandelResponse handler : responseHandlers) {
					handler.handleResponse(response);
				}
			}

		}
		
		/*@Override
		public void handleBy(HandelMessage handler) {
			handler.handleRequest(this);
		}*/
		
		
		
		/*public void setToken(byte[] bs) {
			// TODO Auto-generated method stub
			
		}*/


		/*public Object send() {
			// TODO Auto-generated method stub
			return null;
		}*/
		
		/**
		 * This method to set the request's options for host, port
		 * and path with a string in the following format
		 * <code>[scheme]://[host]:[port]{/resource}*?{&amp;query}*</code>
		 * 
		 * @param uri the URI defining the target resource
		 * @return this request
		 */
		/*public Request setURI(String uri) {
			try {
				if (!uri.startsWith("coap://") && !uri.startsWith("coaps://"))
					uri = "coap://" + uri;
				   setURI(new URI(uri));
				   return this; 
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Failed to set uri "+uri + ": " + e.getMessage());
			}
		}*/
		
		

		private boolean rejected;
		/* To check whether message is rejected
		 * 
		 */
		public boolean isRejected() {
			return rejected;
		}
		
		public Response waitForResponse(long timeout) {
			long begin = System.currentTimeMillis();
			long expired = timeout>0 ? (begin + timeout) : 0;
			while (this.response == null && !isCanceled() && !isTimedOut() && !isRejected()) {
					try {
						lock.wait(timeout);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long currentTime = System.currentTimeMillis();				
					// timeout expired?
					if (timeout > 0 && expired <= currentTime) {
						// end loop since response is empty
						break;
					}
				}
				Response r = this.response;
				this.response = null;
				return r;
			}
		

		private boolean timedOut;
		/*To check whether message is timeout
		 * 
		 */
		private boolean isTimedOut() {
			return timedOut;
		}

		private boolean canceled;
		/*To check if message is cancelled
		 * 
		 */
		private boolean isCanceled() {
			return canceled;
		}


		public void execute() {
			// TODO Auto-generated method stub
			
		}

		
		
	

}

