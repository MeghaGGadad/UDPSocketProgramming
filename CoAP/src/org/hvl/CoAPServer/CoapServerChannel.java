package org.hvl.CoAPServer;

import org.hvl.CoAP.CoAPCodeRegistries.ResponseCode;
import org.hvl.CoAPClient.Request;
import org.hvl.CoAP.*;


public interface CoapServerChannel extends CoapChannel {
	
	/* creates a normal response */
    public Response createResponse(Request request, ResponseCode responseCode);

    /* creates a normal response */
    public Response createResponse(CoapMessage request, ResponseCode responseCode, MediaTypeRegistery contentType);
    
	/* creates a separate response and acks the current request witch an empty ACK in case of a CON.
	 * The separate response can be send later using sendSeparateResponse()  */
	public Response createSeparateResponse(Request request,
			ResponseCode responseCode);

	/* used by a server to send a separate response */
	public void sendSeparateResponse(Response response);
	
	
	/* used by a server to create a notification (observing resources),  reliability is base on the request packet type (con or non) */
	public Response createNotification(Request request, ResponseCode responseCode, int sequenceNumber);
	
	/* used by a server to create a notification (observing resources) */
	public Response createNotification(Request request, ResponseCode responseCode, int sequenceNumber, boolean reliable);
	
	/* used by a server to send a notification (observing resources) */
	public void sendNotification(Response response);






}
