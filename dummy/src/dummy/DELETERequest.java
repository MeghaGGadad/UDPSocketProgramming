package dummy;

public class DELETERequest {
    public DELETERequest(){
    	super();
    }
    public void dispatch(HandelRequest handle) {
		handle.performDELETE(this);
	}
}
