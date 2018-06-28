package org.hvl.CoAPClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.hvl.CoAP.CoAPOptionRegistry;
import org.hvl.CoAP.MessageFormat;
import org.hvl.CoAP.Options;
import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAP.CoAPCodeRegistries.Code;
import org.hvl.CoAP.CoAPCodeRegistries.Type;
import org.hvl.CoAPServer.HandelResponse;
import org.hvl.CoAPServer.Response;



public class Request extends MessageFormat {
	
	private BlockingQueue<Response> responseQueue;
	private static final Response TIMEOUT_RESPONSE = new Response();
	 
	private int responseCount;
	//public void setUriHost(String host);
	
	// list of response handlers that are notified about incoming responses
	private List<HandelResponse> responseHandlers;
	
	private static final long startTime = System.currentTimeMillis();
	
	/** The current response for the request. */
	private Response response;
	
	/** The lock object used to wait for a response. */
	private Object lock;
	
	/** The request code. */
	private CoAPCodeRegistries.Code code;
	
		
	// Constructors ////////////////////////////////////////////////////////////
		
	public Request(Code code) {
		super();
		this.code = code;
	}
		
	/* Constructor for a new CoAP message
	 * @param code The method code of the message
	 * @param confirmable True if the request is to be sent as a CON
	 */ 
		public Request(int code, boolean confirmable) {
			//this.code = code;
			super(confirmable ? 
				Type.CON : Type.NON, 
				code);
		}
		
		
		/* This method 
		 * places a new response to this request, 
		 * 
		 * @param response A response to the request
		 */
		public void respond(Response response) {
			
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
					// use separate response:
					// CON response to CON request, 
					// NON response to NON request
					response.setType(getType());
				}
			}
			
			
			
			// check observe option
			
			Options observeOpt = getFirstOption(CoAPOptionRegistry.OBSERVE);
			if (observeOpt != null && !response.hasOption(CoAPOptionRegistry.OBSERVE)) {
				
				// 16-bit second counter
				int secs = (int)((System.currentTimeMillis() - startTime) / 1000) & 0xFFFF;
				
				response.setOption(new Options(secs, CoAPOptionRegistry.OBSERVE));
				
				if (response.isConfirmable()) {
					response.setType(CoAPCodeRegistries.Type.NON);
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
		public Response waitForResponse() throws InterruptedException {
			return waitForResponse(0);
		}	
		
	private Response waitForResponse(int i) {
			// TODO Auto-generated method stub
			return null;
		}


	public void respond(int code, String message) {
			Response response = new Response(code);
			if (message != null) {
				response.setPayload(message);
			}
			respond(response);
		}

		public void respond(int code) {
			respond(code, null);
		}
		
		public void accept() {
			if (isConfirmable()) {
				Response ack = new Response(CoAPCodeRegistries.EMPTY_MESSAGE);
				ack.setType(CoAPCodeRegistries.Type.ACK);
				respond(ack);
			}
		}

		public void reject() {
			if (isConfirmable()) {
				Response rst = new Response(CoAPCodeRegistries.EMPTY_MESSAGE);
				rst.setType(Type.RST);
				respond(rst);
			}
		}
		
		public void responseCompleted(Response response) {
			// do nothing
		}
		
		
		/*
		 * This method returns a response placed using respond() and
		 * blocks until a response is available.
		 */
		public Response responseReceive() throws InterruptedException {
			
			// response queue required to perform this operation
			if (!responseQueueOn()) {
				System.out.println("WARNING: Responses may be lost, because of  Missing useResponseQueue(true) call, ");
				enableResponseQueue(true);
			}
			
			// receive response from queue
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
		 * Thsi method Registers a handler for responses to this request
		 * 
		 * 
		 */
		public void registerResponseHandler(HandelResponse handler) {

			if (handler != null) {
				
				// lazy creation of response handler list
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
		public void unregisterResponseHandler(HandelResponse handler) {

			if (handler != null && responseHandlers != null) {
				
				responseHandlers.remove(handler);
			}
		}

		/*
		 * Enables or disables the response queue
		 * 
		 * Implementation NOTE: The response queue needs to be ON/enabled BEFORE 
		 *       calls to receiveResponse()
		 * 
		 * @param enable True to enable and false to disable the response queue,
		 * respectively
		 */
		public void enableResponseQueue(boolean enable) {
			if (enable != responseQueueOn()) {
				responseQueue = enable ? new LinkedBlockingQueue<Response>() : null;
			}
		}
		
		/*
		 * Checks if the response queue is enabled
		 * 
		 * NOTE: The response queue needs to be enabled BEFORE any possible
		 *       calls to receiveResponse()
		 * 
		 * @return True iff the response queue is enabled
		 */	
		public boolean responseQueueOn() {
			return responseQueue != null;
		}	
		
		
		
		/*
		 * This method is called whenever a response was placed to this request.
		 * Subclasses can override this method in order to handle responses.
		 * 
		 * @param response The response to handle
		 */
		public void handleResponse(Response response) {

			// enqueue response
			if (responseQueueOn()) {
				if (!responseQueue.offer(response)) {
					System.out.println("ERROR: Failed to enqueue response to request");
				}
			}
		
			// notify response handlers
			if (responseHandlers != null) {
				for ( HandelResponse handler : responseHandlers) {
					handler.handleResponse(response);
				}
			}

		}
		
		/*public void responsePayloadAppended(Response response, byte[] block) {
			// do nothing
		}*/
		
		/*public void responseCompleted(Response response) {
			// do nothing
		}*/
		
		/*
		 * Direct subclasses need to override this method in order to invoke
		 * the according method of the provided RequestHandler (visitor pattern)
		 * 
		 * @param handler A handler for this request
	 */
		public void dispatch(HandelResponse handler) {
			System.out.printf("Unable to dispatch request with code '%s'", 
				CoAPCodeRegistries.toString(getCode()));
		}
		
		/*@Override
		public void handleBy(HandelMessage handler) {
			handler.handleRequest(this);
		}*/
		
		
		
		public void setToken(byte[] bs) {
			// TODO Auto-generated method stub
			
		}


		public Object send() {
			// TODO Auto-generated method stub
			return null;
		}
		
		/**
		 * This is a convenience method to set the reques's options for host, port
		 * and path with a string of the form
		 * <code>[scheme]://[host]:[port]{/resource}*?{&amp;query}*</code>
		 * 
		 * @param uri the URI defining the target resource
		 * @return this request
		 */
		public boolean setURI(String uri) {
			try {
				if (!uri.startsWith("coap://") && !uri.startsWith("coaps://"))
					uri = "coap://" + uri;
				    setURI(new URI(uri));
				    return true;
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Failed to set uri "+uri + ": " + e.getMessage());
			}
		}
		
		public void setUriPath(String uri)
		{
			
		}


		public boolean isRejected() {
			// TODO Auto-generated method stub
			return false;
		}
		
		public Response waitForResponse(long timeout) throws InterruptedException {
			long before = System.currentTimeMillis();
			long expired = timeout>0 ? (before + timeout) : 0;
			// Lazy initialization of a lock
			if (lock == null) {
				synchronized (this) {
					if (lock == null)
						lock = new Object();
				}
			}
			// wait for response
			synchronized (lock) {
				while (this.response == null && !isCanceled() && !isTimedOut() && !isRejected()) {
					lock.wait(timeout);
					long now = System.currentTimeMillis();				
					// timeout expired?
					if (timeout > 0 && expired <= now) {
						// break loop since response is still null
						break;
					}
				}
				Response r = this.response;
				this.response = null;
				return r;
			}
		}


		private boolean isTimedOut() {
			// TODO Auto-generated method stub
			return false;
		}


		private boolean isCanceled() {
			// TODO Auto-generated method stub
			return false;
		}


		public static Request newGet() {
			return new Request(CoAPCodeRegistries.Code.GETRequest); 
		}
		/**
		 * Gets the request code.
		 *
		 * @return the code
		 */
		//public Code getCode() {
			//return code;
		//}

		public void setContentType(Object object) {
			// TODO Auto-generated method stub
			
		}

		public Object getObserveOption() {
			// TODO Auto-generated method stub
			return null;
		}

		public void execute() {
			// TODO Auto-generated method stub
			
		}

		public byte[] getPayload() {
			// TODO Auto-generated method stub
			return null;
		}
		}
	



