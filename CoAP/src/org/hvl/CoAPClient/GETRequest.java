package org.hvl.CoAPClient;

import org.hvl.CoAP.CoAPCodeRegistries;

public class GETRequest extends Request {
	public GETRequest(){
		super(CoAPCodeRegistries.GET, true);
	}
	
	
	
	public void dispatch(HandelRequest handle) {
		handle.performGET(this);
	}
}
