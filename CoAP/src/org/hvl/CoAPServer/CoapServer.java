package org.hvl.CoAPServer;

import org.hvl.CoAP.CoapChannelListener;
import org.hvl.CoAPClient.Request;

public interface CoapServer extends CoapChannelListener {
    public CoapServer onAccept(Request request);
    public void onRequest(CoapServerChannel channel, Request request);
	public void onSeparateResponseFailed(CoapServerChannel channel);

}
