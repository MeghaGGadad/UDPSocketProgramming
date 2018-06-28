package org.hvl.CoAPServer;

import org.hvl.CoAP.CoAPCodeRegistries.ResponseCode;;

public class CoapExchange {

	
		public void respond(ResponseCode content, String payload) {
			respond(ResponseCode.CONTENT, payload);
		}
		
	}


