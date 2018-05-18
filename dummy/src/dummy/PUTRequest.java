package dummy;

  public class PUTRequest {
	public PUTRequest(){
		super();
	}
	public void dispatch(HandelRequest handle) {
		handle.performPUT(this);
	}
}
