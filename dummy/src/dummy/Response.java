package dummy;



public class Response extends MessageFormat {

	public Response() {
		this(messageType.RESP_VALID);
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
	
	public int getRTT() {
		if (request != null) {
			return (int)(getTimestamp() - request.getTimestamp());
		} else {
			return -1; 
		}
	}
	
	private int getTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void handle() {
		if (request != null) {
			request.handleResponse(this);
		}
	}
	
	@Override
	protected void payloadAppended(byte[] block) {
		if (request != null) {
			request.responsePayloadAppended(this, block); 
		}
	}
	
	@Override
	protected void completed() {
		if (request != null) {
			request.responseCompleted(this);
		}
	}
	
	@Override
	public void handleBy(HandlMessage handler) {
		handler.HandelResponse(this);
	}
	
	public boolean isPiggyBacked() {
		return isAcknowledgement() && getCode() != messageType.EMPTY_MESSAGE;
	}

	public boolean isEmptyACK() {
		return isAcknowledgement() && getCode() == messageType.EMPTY_MESSAGE;
	}
	
	
	private boolean isAcknowledgement() {
		// TODO Auto-generated method stub
		return false;
	}

	private int getCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	


	private Request request;




	public boolean hasOption(int observe) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setOption(Options option) {
		// TODO Auto-generated method stub
		
	}

	public Object getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
 


