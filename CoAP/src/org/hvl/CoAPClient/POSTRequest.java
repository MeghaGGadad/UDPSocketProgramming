package org.hvl.CoAPClient;

import org.hvl.CoAP.CoAPCodeRegistries;

public class POSTRequest extends Request {
   public POSTRequest(){
	   super(CoAPCodeRegistries.POST, true);
	   
   }
    
   public void dispatch(HandelRequest handle) {
		handle.performPOST(this);
	}
}
