package org.hvl.CoAPServer;



import org.hvl.CoAP.HandelMessage;
import org.hvl.CoAP.MessageFormat;
import org.hvl.CoAP.Options;
import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAPClient.Request;




public class Response extends MessageFormat {
	
	//create request object
	private Request request;
	/** The response code. */
	//private messageType.ResponseCode code;

	public Response() {
		this(CoAPCodeRegistries.RESP_VALID);
	}
	
	public Response(int code) {
		setCode(code);
	}
	
	public void setRequest(Request request) {
		this.request = request;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public void respond() {
		if (request != null) {
			request.respond(this);
	}
	}
	
	public int getTime() {
		if (request != null) {
			return (int)(getTimestamp() - request.getTimestamp());
		} else {
			return -1; 
		}
	}
	
	
    /* This method handel Response
     * 
     */
	public void handle() {
		if (request != null) {
			request.handleResponse(this);
		}
	}
	
	/*This method mark response completed or not
	 * 
	 */
	
	@Override
	protected void completed() {
		if (request != null) {
			request.responseCompleted(this);
		}
	}
	
	@Override
	public void handleBy(HandelMessage handler) {
		handler.HandelResponse(this);
	}
	
	/**
	 * Gets the response code.
	 *
	 * @return the code
	 */
	//public messageType.ResponseCode getCode() {
		//return code;
	//}
   public boolean hasOption(int observe) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setOption(Options option) {
		// TODO Auto-generated method stub
		
	}

	public char[] getResponseText() {
		// TODO Auto-generated method stub
		return null;
	}
	/* For more information can be found in RFC7252 5.2.1. Piggybacked  
	 *  
	 */
	public boolean isPiggyBacked() {
		return isAcknowledgement() && getCode() != CoAPCodeRegistries.EMPTY_MESSAGE;
	}
	
		public boolean isEmptyACK() {
			return isAcknowledgement() && getCode() == CoAPCodeRegistries.EMPTY_MESSAGE;
		}

		public boolean hasFormat(int linkFormat) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	


 


