package org.hvl.CoAPClient;

public interface HandelRequest {
	
	public void performGET(GETRequest request);
	public void performPOST(POSTRequest request);
	public void performPUT(PUTRequest request);
	public void performDELETE(DELETERequest request);

}
