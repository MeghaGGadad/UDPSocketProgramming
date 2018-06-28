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
	
	//12.1.2. Response Codes
	
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
	
	public static final int RESP_REQUEST_ENTITY_TOO_LARGE         = 141;
	
	public static final int RESP_UNSUPPORTED_MEDIA_TYPE           = 143;
	//Server error: 160--192
	public static final int RESP_INTERNAL_SERVER_ERROR            = 160;
	public static final int RESP_NOT_IMPLEMENTED                  = 161;
	public static final int RESP_BAD_GATEWAY                      = 162;
	public static final int RESP_SERVICE_UNAVAILABLE              = 163;
	public static final int RESP_GATEWAY_TIMEOUT                  = 164;
	public static final int RESP_PROXYING_NOT_SUPPORTED           = 165;

	// from draft-ietf-core-block-03
	public static final int RESP_REQUEST_ENTITY_INCOMPLETE        = 136;
	
	// 12.1.2. Response Codes
	public static final int V3_RESP_CONTINUE                      = 40;
	public static final int V3_RESP_OK                            = 80;
	public static final int V3_RESP_CREATED                       = 81;
	public static final int V3_RESP_NOT_MODIFIED                  = 124;
	public static final int V3_RESP_BAD_REQUEST                   = 160;
	public static final int V3_RESP_NOT_FOUND                     = 164;
	public static final int V3_RESP_METHOD_NOT_ALLOWED            = 165;
	public static final int V3_RESP_UNSUPPORTED_MEDIA_TYPE        = 175;
	public static final int V3_RESP_INTERNAL_SERVER_ERROR         = 200;
	public static final int V3_RESP_BAD_GATEWAY                   = 202;
	public static final int V3_RESP_SERVICE_UNAVAILABLE           = 203;
	public static final int V3_RESP_GATEWAY_TIMEOUT               = 204;
	public static final int V3_RESP_TOKEN_OPTION_REQUIRED         = 240;
	public static final int V3_RESP_URI_AUTHORITY_OPTION_REQUIRED = 241;
	public static final int V3_RESP_CRITICAL_OPTION_NOT_SUPPORTED = 242; 
	
	
	
	//message type
	public static final int CON = 0;
	public static final int NON = 1;
	public static final int ACK = 2;
	public static final int RST = 3;
	
	//Checks whether a code indicates a request 
	public static boolean isRequest(int code) {
		return (code >= 1) && (code <= 31);
	}
	
    //code indicates a response 
	public static boolean isResponse(int code) {
		return (code >= 40) && (code <= 242);
	}
	
	//to check whether a code is valid
	public static boolean isValid(int code) {
		return (code >= 0) && (code <= 255);
		
	}
	
	//return response class
	public static int responseClass(int code) {
		return (code >> 5) & 0x7;
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
			
			/** The GET code. */
			GETRequest(1),
			

			/** The POST code. */
			POSTRequest(2),
			
			/** The PUT code. */
			PUTRequest(3),
			
			/** The DELETE code. */
			DELETERequest(4);
			
			/** The code value. */
			public final int value;
			
			/**
			 * Instantiates a new code with the specified code value.
			 *
			 * @param value the integer value of the code
			 */
			Code(int value) {
				this.value = value;
			}
			
		
			
			/**
			 * Converts the specified integer value to a request code.
			 *
			 * @param value the integer value
			 * @return the request code
			 * @throws IllegalArgumentException if the integer value is unrecognized
			 */
		public static Code valueOf(int value) {
			switch (value) {
				case 1: return GETRequest;
				case 2: return POSTRequest;
				case 3: return PUTRequest;
				case 4: return DELETERequest;
				default: throw new IllegalArgumentException("Unknwon CoAP request code "+value);
			}
		}
	}

		/**
		 * The enumeration of response codes
		 */
		public enum ResponseCode {
			
			// Success: 64--95
			_UNKNOWN_SUCCESS_CODE(64), // 2.00 is undefined -- only used to identify class
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
			public final int value;
			
			
			
			/**
			 * Instantiates a new response code with the specified integer value.
			 *
			 * @param value the integer value
			 */
			private ResponseCode(int value) {
				this.value = value;
			}
			
			/**
			 * Converts the specified integer value to a response code.
			 *
			 * @param value the value
			 * @return the response code
			 * @throws IllegalArgumentException if integer value is not recognized
			 */
			public static ResponseCode valueOf(int value) {
				switch (value) {
					// CoAPTest.testResponseCode ensures we keep this up to date 
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
					// codes unknown at release time
					default:
						// Fallback to class
						if (value/32 == 2) return _UNKNOWN_SUCCESS_CODE;
						else if (value/32 == 4) return BAD_REQUEST;
						else if (value/32 == 5) return INTERNAL_SERVER_ERROR;
						/// Undecidable
						else throw new IllegalArgumentException("Unknown CoAP response code "+value);
				}
			}
			
			public String toString() {
				return String.format("%d.%02d", this.value/32, this.value%32);
			}
			
			public static boolean isSuccess(ResponseCode code) {
				return 64 <= code.value && code.value < 96;
			}
			
			public static boolean isClientError(ResponseCode code) {
				return 128 <= code.value && code.value < 160;
			}
			
			public static boolean isServerError(ResponseCode code) {
				return 160 <= code.value && code.value < 192;
			}
		}
			
		/*
		 * Returns a string representation of the code
		 * 
		 * @param code The code to describe
		 * @return A string describing the code
		 */
		public static String toString(int code) {

		switch (code) {
			case EMPTY_MESSAGE:
				return "Empty Message";
					
					case 1:    
						return "GET Request";
					case 2:   
						return "POST Request";
					case 3:    
						return "PUT Request";
					case 4: 
						return "DELETE Request";
					
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
					case RESP_REQUEST_ENTITY_TOO_LARGE: 
						return "4.13 Request Entity Too Large";
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
						
					// Deprecated (Draft 3)
					case V3_RESP_CONTINUE:
						return "100 Continue";
					case V3_RESP_OK:
						return "200 OK";
					case V3_RESP_CREATED:
						return "201 Created";
					case V3_RESP_NOT_MODIFIED:
						return "304 Not Modified";
					case V3_RESP_UNSUPPORTED_MEDIA_TYPE:
						return "415 Unsupported Media Type";
					case V3_RESP_INTERNAL_SERVER_ERROR:
						return "500 Internal Server Error";
					case V3_RESP_BAD_GATEWAY:
						return "502 Bad Gateway";
					case V3_RESP_SERVICE_UNAVAILABLE:
						return "503 Service Unavailable";
					case V3_RESP_GATEWAY_TIMEOUT:
						return "504 Gateway Timeout";
					case V3_RESP_TOKEN_OPTION_REQUIRED:
						return "Token Option required by server";
					case V3_RESP_URI_AUTHORITY_OPTION_REQUIRED:
						return "Uri-Authority Option required by server";
					case V3_RESP_CRITICAL_OPTION_NOT_SUPPORTED:
						return "Critical Option not supported";
					}
					
					if (isValid(code)) {
						
						if (isRequest(code)) {
							return String.format("Unknown Request [code %d]", code);
						} else if (isResponse(code)) {
							return String.format("Unknown Response [code %d]", code);
						} else {
							return String.format("Reserved [code %d]", code);
						}
						
					} else {
						return String.format("Invalid Message [code %d]", code);
					}
				}
			}
		
