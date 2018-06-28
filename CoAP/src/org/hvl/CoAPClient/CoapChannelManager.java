package org.hvl.CoAPClient;

import java.net.InetAddress;

import org.hvl.CoAPServer.BasicServer;
import org.hvl.CoAPServer.CoapServer;



public interface CoapChannelManager {

	public int getNewMessageID();

    /* called by the socket Listener to create a new Server Channel
     * the Channel Manager then asked the Server Listener if he wants to accept a new connection */
	//public CoapServerChannel createServerChannel(CoapSocketHandler socketHandler, CoapMessage message, InetAddress addr, int port);

	/* creates a server socket listener for incoming connections */
    public void createServerListener(BasicServer server, int localPort);

    /* called by a client to create a connection
     * TODO: allow client to bind to a special port */
    public CoapClientChannel connect(BasicClient basicClient, InetAddress addr, int port);
    
    /* This function is for testing purposes only, to have a determined message id*/
    public void setMessageId(int globalMessageId);
    
    public void initRandom();

	//public void createServerListener(BasicServer server, int port);


}
