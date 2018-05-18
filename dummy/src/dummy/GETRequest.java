package dummy;



public class GETRequest {
	public GETRequest(){
		//super(messageType.GET, true);
	}
	
	
	
	public void dispatch(HandelRequest handle) {
		handle.performGET(this);
	}
}
