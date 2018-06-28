package org.hvl.CoAP;

public class MediaTypeRegistery {
	
	//section 12.3 CoAP Content-Formats Registry
	public static final int PLAIN             = 0;
	public static final int XML               = 41;
	public static final int OCTET_STREAM      = 42;
	public static final int EXI               = 47;
	public static final int JSON              = 50;
	
	public static final int UNDEFINED = -1;
	
	// draft-ietf-core-link-format-04, section 7.4
	public static final int LINK_FORMAT       = 40;
	
	public static boolean isPrintable(int mediaType) {
		switch (mediaType) {
		case PLAIN:
		case XML:
		case OCTET_STREAM:
		case JSON:
        case UNDEFINED:
			return true;

		
		default:
			return false;
		}
	}

	
	public static String toString(int mediaType) {
		switch (mediaType) {
		case PLAIN:
			return "text/plain; charset=utf-8";
		case LINK_FORMAT:
			return "application/link-format";
		case XML:
			return "application/xml";
		case OCTET_STREAM:
			return "application/octet-stream";
		case EXI:
			return "application/exi";
		case JSON:
			return "application/json";
		default:
			return "Unknown media type: " + mediaType;
		}
	}


	public static Object parse(Integer type) {
		// TODO Auto-generated method stub
		return null;
	}
}


