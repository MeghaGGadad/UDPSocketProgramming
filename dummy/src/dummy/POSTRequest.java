package dummy;

public class POSTRequest {
   public POSTRequest(){
	   super();
	   
   }
    
   public void dispatch(HandelRequest handle) {
		handle.performPOST(this);
	}
}
