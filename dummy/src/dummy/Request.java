package dummy;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



public class Request extends MessageFormat {
	
	// list of response handlers that are notified about incoming responses
	private List<HandelResponse> responseHandlers;
		
		// Constructors ////////////////////////////////////////////////////////////
		
		//Constructor for a new CoAP message
		 
		public Request(int code, boolean confirmable) {
			super(confirmable ? 
				messageType.Type.CON : messageType.Type.NON, 
				code);
		}
		
		
		
		private static final long startTime = System.currentTimeMillis();
		
		/*
		 * Places a new response to this request, e.g. to answer it
		 * 
		 * @param response A response to this request
		 */
		public void respond(Response response) {
			
			// assign response to this request
			response.setRequest(this);
			
			response.setURI(getURI());
			response.setOption(getFirstOption(CoAP_Opt_num_Registry.TOKEN));

			if (responseCount == 0 && isConfirmable()) {
				response.setID(getID());
			}
			
			// set message type
			if (response.getType() == null) {
				if (responseCount == 0 && isConfirmable()) {
					// use piggy-backed response
					response.setType(messageType.Type.ACK);
				} else {
					// use separate response:
					// Confirmable response to confirmable request, 
					// Non-confirmable response to non-confirmable request
					response.setType(getType());
				}
			}
			
			// check observe option
			
			Options observeOpt = getFirstOption(CoAP_Opt_num_Registry.OBSERVE);
			if (observeOpt != null && !response.hasOption(CoAP_Opt_num_Registry.OBSERVE)) {
				
				// 16-bit second counter
				int secs = (int)((System.currentTimeMillis() - startTime) / 1000) & 0xFFFF;
				
				response.setOption(new Options(secs, CoAP_Opt_num_Registry.OBSERVE);
				
				if (response.isConfirmable()) {
					response.setType(messageType.Type.NON);
				}
			}
			
			// check if response is of remote origin, i.e.
			// was received by a communicator
			if (communicator != null) try {
				communicator.sendMessage(response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} else {
				
				// handle locally
				response.handle();
			}

			++responseCount;
		}
		
		private boolean isConfirmable() {
			// TODO Auto-generated method stub
			return false;
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
				Response ack = new Response(MessageFormat.EMPTY_MESSAGE);
				ack.setType(messageType.Type.ACK);
				respond(ack);
			}
		}

		public void reject() {
			if (isConfirmable()) {
				Response rst = new Response(MessageFormat.EMPTY_MESSAGE);
				rst.setType(messageType.Type.RST);
				respond(rst);
			}
		}
		
		public void setCommunicator(Communicator communicator) {
			this.communicator = communicator;
		}
		
		/*
		 * Returns a response that was placed using respond() and
		 * blocks until such a response is available.
		 * 
		 * NOTE: In order to safely use this method, the call useResponseQueue(true)
		 * is required BEFORE any possible respond() calls take place
		 * 
		 * @return The next response that was placed using respond()
		 */
		public Response receiveResponse() throws InterruptedException {
			
			// response queue required to perform this operation
			if (!responseQueueEnabled()) {
				System.out.println("WARNING: Missing useResponseQueue(true) call, responses may be lost");
				enableResponseQueue(true);
			}
			
			// take response from queue
			Response response = responseQueue.take();
			
			// return null if request timed out
			return response != TIMEOUT_RESPONSE ? response : null; 
		}
		
		@Override
		public void timedOut() {
			if (responseQueueEnabled()) {
				responseQueue.offer(TIMEOUT_RESPONSE);
			}
		}

		/*
		 * Registers a handler for responses to this request
		 * 
		 * @param handler The observer to add to the handler list
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
		 * Unregisters a handler for responses to this request
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
		 * NOTE: The response queue needs to be enabled BEFORE any possible
		 *       calls to receiveResponse()
		 * 
		 * @param enable True to enable and false to disable the response queue,
		 * respectively
		 */
		public void enableResponseQueue(boolean enable) {
			if (enable != responseQueueEnabled()) {
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
		public boolean responseQueueEnabled() {
			return responseQueue != null;
		}	
		
		// Subclassing /////////////////////////////////////////////////////////////
		
		/*
		 * This method is called whenever a response was placed to this request.
		 * Subclasses can override this method in order to handle responses.
		 * 
		 * @param response The response to handle
		 */
		protected void handleResponse(Response response) {

			// enqueue response
			if (responseQueueEnabled()) {
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
		
		protected void responsePayloadAppended(Response response, byte[] block) {
			// do nothing
		}
		
		protected void responseCompleted(Response response) {
			// do nothing
		}
		
		/*
		 * Direct subclasses need to override this method in order to invoke
		 * the according method of the provided RequestHandler (visitor pattern)
		 * 
		 * @param handler A handler for this request
		 */
		public void dispatch(HandelResponse handler) {
			System.out.printf("Unable to dispatch request with code '%s'", 
				messageType.toString(getCode()));
		}
		
		@Override
		public void handleBy(MessageHandler handler) {
			handler.handleRequest(this);
		}
		
		// Class functions /////////////////////////////////////////////////////////

		/*
		 * Returns the default communicator used for outgoing requests
		 * 
		 * @return The default communicator
		 */
		public static Communicator defaultCommunicator() {
			
			// lazy initialization
			if (DEFAULT_COMM == null) {
				try {
					DEFAULT_COMM = new Communicator();
				} catch (SocketException e) {
					System.out.printf("[%s] Failed to create default communicator: %s\n", 
						"JCoAP", e.getMessage());
				}
			}
			return DEFAULT_COMM;
		}
			
		// Class attributes ////////////////////////////////////////////////////////
		
		// the default communicator for request objects (lazy initialized)
		private static Communicator DEFAULT_COMM;
		
		private static final Response TIMEOUT_RESPONSE
			= new Response();
		
		// Attributes //////////////////////////////////////////////////////////////
		
		private Communicator communicator;
		
		
		
		
		// queue used to store responses that will be retrieved using 
		// receiveResponse() 
		private BlockingQueue<Response> responseQueue;
		
		// number of responses to this request
		private int responseCount;

		public int getTimestamp() {
			// TODO Auto-generated method stub
			return 0;
		}
	}


}
