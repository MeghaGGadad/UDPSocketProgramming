package org.hvl.CoAP;

import org.hvl.CoAPClient.Request;
import org.hvl.CoAPServer.Response;

import org.hvl.CoAP.MediaTypeRegistery;


public class PrettyPrint {
	
	
	
	/**
	 * Formats a {@link Request} into a readable String representation. 
	 * 
	 * @param r the Request
	 * @return the pretty print
	 */
	public static String prettyPrint(Request r) {
	
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("==[ CoAP Request ]=============================================\n");
	        sb.append(String.format("MID    : %d\n", r.getID()));
	        sb.append(String.format("Token  : %s\n", r.getToken()));
	        sb.append(String.format("Type   : %s\n", r.getType().toString()));
	        //sb.append(String.format("Method : %s\n", r.getCode().toString()));
	        sb.append(String.format("Options: %s\n", r.getOptions().toString()));
	        sb.append(String.format("Payload: %d Bytes\n", r.getpayloadSize()));
	        if (r.getpayloadSize() > 0 && MediaTypeRegistery.isPrintable(r.getOptions().getContentFormat())) {
	        
	        	sb.append("---------------------------------------------------------------");
	        	sb.append(r.getPayloadString());
	        }
	        sb.append("===============================================================");
	
	        return sb.toString();
	}

	
	/**
	 * Formats a {@link Response} into a readable String representation. 
	 * 
	 * @param r the Response
	 * @return the pretty print
	 */
	public static String prettyPrint(Response r) {
	
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("==[ CoAP Response ]============================================\n");
	        sb.append(String.format("MID    : %d\n", r.getID()));
	        sb.append(String.format("Token  : %s\n", r.getToken()));
	        sb.append(String.format("Type   : %s\n", r.getType().toString()));
	        //sb.append(String.format("Status : %s\n", r.getCode().toString()));
	        sb.append(String.format("Options: %s\n", r.getOptions().toString()));
	        sb.append(String.format("Payload: %d Bytes\n", r.getpayloadSize()));
	        if (r.getpayloadSize() > 0 && MediaTypeRegistery.isPrintable(r.getOptions().getContentFormat())) {
	        	sb.append("---------------------------------------------------------------\n");
	        	sb.append(r.getPayloadString());
	        	sb.append("\n");
	        }
	        sb.append("===============================================================");
	
	        return sb.toString();
	}
	

}
