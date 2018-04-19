package dummy;

public class CoAP_Opt_num_Registry {

	//section 12.2. CoAP Option Numbers Registry
	public static final int RESERVED_Zero  = 0;
	public static final int  If_Match      = 1;
	public static final int URI_HOST       = 3;
	public static final int ETAG = 4;
	public static final int IF_NONE_Match = 5;
	public static final int URI_PORT     = 7;
	public static final int LOCATION_PATH       = 8;
	public static final int URI_PATH  = 11;
	public static final int Content_Format = 12;
	public static final int MAX_AGE = 14;
	public static final int URI_QUERY = 15;
	public static final int Accept = 17;
	public static final int LOCATION_QUERY = 20;
	public static final int PROXY_URI = 35;
	public static final int TOKEN               = 13;
	
	public static String toString(int optionNumber) {
		switch (optionNumber) {
		case RESERVED_Zero:
			return "Reserved (0)";
		case URI_HOST:
			return "Uri-Host";
		case ETAG:
			return "ETag";
		case MAX_AGE:
			return "Max-Age";
		case PROXY_URI:
			return "Proxy-Uri";
		case LOCATION_PATH:
			return "Location-Path";
		case URI_PORT:
			return "Uri-Port";
		case LOCATION_QUERY:
			return "Location-Query";
		case URI_PATH:
			return "Uri-Path";
		case URI_QUERY:
			return "Uri-Query";
		
		}
		return String.format("Unknown option [number %d]", optionNumber);
	}
	
	
}
