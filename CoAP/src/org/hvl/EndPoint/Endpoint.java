package org.hvl.EndPoint;

import java.io.IOException;


import org.hvl.CoAP.HandelMessage;
import org.hvl.CoAP.MessageFormat;
import org.hvl.CoAP.ReceiveMessage;
import org.hvl.CoAPClient.Request;


public abstract class Endpoint implements ReceiveMessage, HandelMessage {

		public static final int DEFAULT_PORT = 5683;
		
		
		public abstract void execute(Request request) throws IOException;
		
		
		
		@Override
		public void receiveMessage(MessageFormat msg) {
			msg.handleBy(this);
		}
		
		


}
