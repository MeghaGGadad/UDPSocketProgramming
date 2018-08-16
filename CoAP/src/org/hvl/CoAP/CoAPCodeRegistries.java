package org.hvl.CoAP;



/**This class defines 12.1. CoAP Code Registries
 * 12.1.1. Method Codes
 * 12.1.2. Response Codes
 * @author MeghaGadad
 *
 */

public class CoAPCodeRegistries {
	
	//constant
	public static final int EMPTY_MESSAGE = 0;
	
	//12.1.1 method code
	public static final int GET    = 1;
	public static final int POST   = 2;
	public static final int PUT    = 3;
	public static final int DELETE = 4;
	
	//12.1.2. Response Codes refer Table 6: CoAP Response Codes
	
	public static final int RESP_CLASS_SUCCESS                    = 2;
	public static final int RESP_CLASS_CLIENT_ERROR               = 4;
	public static final int RESP_CLASS_SERVER_ERROR               = 5;
	
	//Success: 64--95
	public static final int RESP_CREATED                          = 65;
	public static final int RESP_DELETED                          = 66;
	public static final int RESP_VALID                            = 67;
	public static final int RESP_CHANGED                          = 68;
	public static final int RESP_CONTENT                          = 69;
	
	//Client error: 128--159
	public static final int RESP_BAD_REQUEST                      = 128;
	public static final int RESP_UNAUTHORIZED                     = 129;
	public static final int RESP_BAD_OPTION                       = 130;
	public static final int RESP_FORBIDDEN                        = 131;
	public static final int RESP_NOT_FOUND                        = 132;
	public static final int RESP_METHOD_NOT_ALLOWED               = 133;
	
	//public static final int RESP_REQUEST_ENTITY_TOO_LARGE         = 141;
	
	public static final int RESP_UNSUPPORTED_MEDIA_TYPE           = 143;
	
	//Server error: 160--192
	public static final int RESP_INTERNAL_SERVER_ERROR            = 160;
	public static final int RESP_NOT_IMPLEMENTED                  = 161;
	public static final int RESP_BAD_GATEWAY                      = 162;
	public static final int RESP_SERVICE_UNAVAILABLE              = 163;
	public static final int RESP_GATEWAY_TIMEOUT                  = 164;
	public static final int RESP_PROXYING_NOT_SUPPORTED           = 165;

	//message type
	public static final int CON = 0;
	public static final int NON = 1;
	public static final int ACK = 2;
	public static final int RST = 3;
	
	//Checks whether a code indicates a request (see Section 12.1.1) 
	public static boolean isRequest(int code) {
		return (code >= 1) && (code <= 31);
	}
	
    //code indicates a response Section 12.1.2
	public static boolean isResponse(int code) {
		return (code >= 40) && (code <= 242);
		 
	}
	
	//to check whether a code is valid
	public static boolean isValidCode(int code) {
		return (code >= 0) && (code <= 255);
		
	}
	
	private CoAPCodeRegistries(){
		
	}
	/**
		 * CoAP defines four types of messages:
		 * Confirmable, Non-confirmable, Acknowledgment, Reset.
	*/
		public enum Type {
			CON(0),
			NON(1),ACK(2),RST(3);
			
			public final int value;
			
			Type(int value) {
				this.value = value;
			}
			
			public static Type valueOf(int value) {
				switch (value) {
					case 0: return CON;
					case 1: return NON;
					case 2: return ACK;
					case 3: return RST;
					default: throw new IllegalArgumentException("Unknown CoAP type "+value);
				}
		}

	}
		public enum Code {
			
			/*The GET code*/
			GETRequest(1),
			/*The POST code*/
			POSTRequest(2),
		    /*The PUT code*/
			PUTRequest(3),
			/*The DELETE code*/
			DELETERequest(4);
			
		    /*The code value*/
			public final int val;
			
			/** This method instantiates a new code with the defined code value.
			 *
			 * @param val the integer value of the code
			 */
			Code(int value) {
				this.val = value;
			}
			
		
			
			/** This method used for
			 * converting the specified integer value to a request code
			 * GET,PUT,POST and DELETE.
			 * @param val the integer value
			 * @return the request code
			 * @throws exception like IllegalArgumentException if the integer val is unrecognized
			 */
		public static Code valueOf(int val) {
			switch (val) {
				case GET: return GETRequest;
				case POST: return POSTRequest;
				case PUT: return PUTRequest;
				case DELETE: return DELETERequest;
				default: throw new IllegalArgumentException("Unknwon CoAP request code "+val);
			}
		}
	}

		/**
		 * Method to define enumeration of response codes
		 */
		public enum ResponseCode {
			
			// Success: 64--95 for more detail see 5.9. Response Code Definitions
			UNKNOWN_SUCCESS_CODE(64), 
			CREATED(65),
			DELETED(66),
			VALID(67),
			CHANGED(68),
			CONTENT(69),
			CONTINUE(95),

			// Client error: 128--159
			BAD_REQUEST(128),
			UNAUTHORIZED(129),
			BAD_OPTION(130),
			FORBIDDEN(131),
			NOT_FOUND(132),
			METHOD_NOT_ALLOWED(133),
			NOT_ACCEPTABLE(134),
			REQUEST_ENTITY_INCOMPLETE(136),
			PRECONDITION_FAILED(140),
			REQUEST_ENTITY_TOO_LARGE(141), 
			UNSUPPORTED_CONTENT_FORMAT(143),

			// Server error: 160--192
			INTERNAL_SERVER_ERROR(160),
			NOT_IMPLEMENTED(161),
			BAD_GATEWAY(162),
			SERVICE_UNAVAILABLE(163),
			GATEWAY_TIMEOUT(164),
			PROXY_NOT_SUPPORTED(165);
			
			/** The code value. */
			public final int val;
			
		/** This function instantiates a new response code with the specified integer value.
			 *
			 * @param value the integer val
		*/
			private ResponseCode(int value) {
				this.val = value;
			}
			
			/**
			 * This method converts the specified int val to a response code.
			 *
			 * @param val the value
			 * @return the response code
			 * @throws IllegalArgumentException if int val is not recognized
			 */
			public static ResponseCode valueOf(int val) {
				switch (val) {
					
					case 65: return CREATED;
					case 66: return DELETED;
					case 67: return VALID;
					case 68: return CHANGED;
					case 69: return CONTENT;
					case 95: return CONTINUE;
					case 128: return BAD_REQUEST;
					case 129: return UNAUTHORIZED;
					case 130: return BAD_OPTION;
					case 131: return FORBIDDEN;
					case 132: return NOT_FOUND;
					case 133: return METHOD_NOT_ALLOWED;
					case 134: return NOT_ACCEPTABLE;
					case 136: return REQUEST_ENTITY_INCOMPLETE;
					case 140: return PRECONDITION_FAILED;
					case 141: return REQUEST_ENTITY_TOO_LARGE;
					case 143: return UNSUPPORTED_CONTENT_FORMAT;
					case 160: return INTERNAL_SERVER_ERROR;
					case 161: return NOT_IMPLEMENTED;
					case 162: return BAD_GATEWAY;
					case 163: return SERVICE_UNAVAILABLE;
					case 164: return GATEWAY_TIMEOUT;
					case 165: return PROXY_NOT_SUPPORTED;
					
					default:
						
						if (val/32 == 2) return UNKNOWN_SUCCESS_CODE;
						else if (val/32 == 4) return BAD_REQUEST;
						else if (val/32 == 5) return INTERNAL_SERVER_ERROR;
						
						else throw new IllegalArgumentException("Unspecified response code "+val);
				}
			}
			
			public static boolean isSuccessfull(ResponseCode code) {
				return 64 <= code.val && code.val < 96;
			}
			
			public static boolean isClientError(ResponseCode code) {
				return 128 <= code.val && code.val < 160;
			}
			
			public static boolean isServerError(ResponseCode code) {
				return 160 <= code.val && code.val < 192;
			}
		}
			
		/*
		 * This function returns a string representation of the code
		 * 
		 * @param code The code 
		 * @return A string describing the code
		 */
		public static String toString(int code) {

		switch (code) {
			case EMPTY_MESSAGE:
				return "Empty Message";
			case GET:    
				return "GETRequest";
			case POST:   
				return "POSTRequest";
			case PUT:    
				return "PUTRequest";
			case DELETE: 
				return "DELETERequest";
					
			case RESP_CREATED: 
						return "2.01 Created";
			case RESP_DELETED: 
						return "2.02 Deleted";
			case RESP_VALID: 
						return "2.03 Valid";
			case RESP_CHANGED: 
						return "2.04 Changed";
			case RESP_CONTENT: 
						return "2.05 Content";
			case RESP_BAD_REQUEST: 
						return "4.00 Bad Request";
		    case RESP_UNAUTHORIZED: 
						return "4.01 Unauthorized";
			case RESP_BAD_OPTION: 
						return "4.02 Bad Option";
			case RESP_FORBIDDEN: 
						return "4.03 Forbidden";
			case RESP_NOT_FOUND: 
						return "4.04 Not Found";
			case RESP_METHOD_NOT_ALLOWED: 
						return "4.05 Method Not Allowed";
			case RESP_UNSUPPORTED_MEDIA_TYPE: 
						return "4.15 Unsupported Media Type";
			case RESP_INTERNAL_SERVER_ERROR: 
						return "5.00 Internal Server Error";
			case RESP_NOT_IMPLEMENTED: 
						return "5.01 Not Implemented";
			case RESP_BAD_GATEWAY: 
						return "5.02 Bad Gateway";
			case RESP_SERVICE_UNAVAILABLE: 
						return "5.03 Service Unavailable";
			case RESP_GATEWAY_TIMEOUT: 
						return "5.04 Gateway Timeout";
			case RESP_PROXYING_NOT_SUPPORTED: 
						return "5.05 Proxying Not Supported";
						
		        }
						
			if (isValidCode(code)) {
						
						if (isRequest(code)) {
							return String.format("Request Unknown [code %d]", code);
						} else if (isResponse(code)) {
							return String.format("Response Unknown[code %d]", code);
						} else {
							return String.format("Reserved for future use [code %d]", code);
						}
						
			 } else {
						return String.format("Invalid Message code [code %d]", code);
					}
					
		        }
	 }		
				
			
		
