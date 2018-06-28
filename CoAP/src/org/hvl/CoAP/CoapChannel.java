package org.hvl.CoAP;

import java.net.InetAddress;



public interface CoapChannel {
	
	public void sendMessage(CoapMessage msg);

	/*TODO: close when finished, & abort()*/
    public void close();
    
    public InetAddress getRemoteAddress();

    public int getRemotePort();
    
    /* handles an incomming message */
    public void handleMessage(CoapMessage message);
    
    /*TODO: implement Error Type*/
	public void lostConnection(boolean notReachable, boolean resetByServer);
	
	
	//public CoapBlockSize getMaxReceiveBlocksize();

	//public void setMaxReceiveBlocksize(CoapBlockSize maxReceiveBlocksize);

	//public CoapBlockSize getMaxSendBlocksize();

	//public void setMaxSendBlocksize(CoapBlockSize maxSendBlocksize);
}



