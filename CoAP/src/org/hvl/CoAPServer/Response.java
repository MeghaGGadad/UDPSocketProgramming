package org.hvl.CoAPServer;

import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAP.HandelMessage;
import org.hvl.CoAP.MessageFormat;
import org.hvl.CoAPClient.Request;

public class Response extends MessageFormat {
	
	private Request request;
	
	
	
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
			request.respondback(this);
		}
	}
	
	public int getTime() {
		if (request != null) {
			return (int)(getTimestamp() - request.getTimestamp());
		} else {
			return -1; 
		}
	}
	
	public void handle() {
		if (request != null) {
			request.responseHandel(this);
		}
	}
	
	/*@Override
	protected void payloadAppended(byte[] block) {
		if (request != null) {
			request.responsePayloadAppended(this, block); 
		}
	}*/
	
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
	
	public boolean isPiggyBacked() {
		return isAcknowledgement() && getCode() != CoAPCodeRegistries.EMPTY_MESSAGE;
	}

	public boolean isEmptyACK() {
		return isAcknowledgement() && getCode() == CoAPCodeRegistries.EMPTY_MESSAGE;
	}

}
	
	
	


 


