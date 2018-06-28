/*package org.hvl.CoAPClient;

import java.io.IOException;
import java.util.HashMap;




public class BasicCoapChannelManager {

	
	private HashMap<Integer, SocketInformation> socketMap = new HashMap<Integer, SocketInformation>();
	public static CoapChannelManager getInstance() {
		// TODO Auto-generated method stub
		return null;
	}
    
	public void createServerListener(CoapServer serverListener, int localPort) {
        if (!socketMap.containsKey(localPort)) {
            try {
            	SocketInformation socketInfo = new SocketInformation(new BasicCoapSocketHandler(this, localPort), serverListener);
            	socketMap.put(localPort, socketInfo);
            } catch (IOException e) {
				e.printStackTrace();
			}
        } else {
        	/*TODO: raise exception: address already in use */
        	/*throw new IllegalStateException();
        }
    }
	
/*	private class SocketInformation {
		public CoapSocketHandler socketHandler = null;
		public CoapServer serverListener = null;
		public SocketInformation(CoapSocketHandler socketHandler,
				CoapServer serverListener) {
			super();
			this.socketHandler = socketHandler;
			this.serverListener = serverListener;
		}
	}
}*/
