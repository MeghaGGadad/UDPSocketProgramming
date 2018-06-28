package org.hvl.CoAP;

public class CoAPOptionRegistry {

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
	public static final int OBSERVE             = 10; 
	public static final int CONTENT_TYPE        = 1;
	
	private Integer      content_format;
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
	
	//defines different type of option format section 3.2. Option Value Formats
	public static enum optionFormats {
		integer,
		string,
		opaque,
		unknown,
		error
	}
	
	
	/*
	 * This method returns an Option with it's default value corresponding to
	 * a given option number
	 * 
	 * @param optionNumber The given option number
	 * @return An option corresponding to the given option number containing
	 *         the default value as specified in Table 4: Options
	 */
	public static Options getDefaultOption (int optionNumber) {
		switch(optionNumber) {
		
		case MAX_AGE:
			return new Options (60, MAX_AGE);
		case PROXY_URI:
			return new Options ("", PROXY_URI);
		case ETAG:
			return new Options (new byte[0], ETAG);
		case URI_HOST:
			//Use function which takes the IP as an argument
			return null;
		case LOCATION_PATH:
			return new Options ("", LOCATION_PATH);
		case URI_PORT:
			//Use function which takes the UDP port as an argument
			return null;
		case LOCATION_QUERY:
			return new Options ("", LOCATION_QUERY);
		case URI_PATH:
			return new Options ("", URI_PATH);
		case TOKEN:
			return new Options (new byte[0], TOKEN);
		case URI_QUERY:
			return new Options ("", URI_QUERY);
		default:
			return null;
		}
	}
	
	
	/**As defined in the section 5.10. Option Definitions
	 * Returns the option format based on the option number
	 *@param optionNumber The option number
	 * @return The option format corresponding to the option number
	 */
	public static optionFormats getFormatByNum (int optionNumber) {
		switch (optionNumber) {
		case RESERVED_Zero:
			return optionFormats.unknown;
		case PROXY_URI:
			return optionFormats.string;
		case ETAG:
			return optionFormats.opaque;
		case URI_HOST:
			return optionFormats.string;
		case LOCATION_PATH:
			return optionFormats.string;
		case URI_PORT:
			return optionFormats.integer;
		case LOCATION_QUERY:
			return optionFormats.string;
		case URI_PATH:
			return optionFormats.string;
		case TOKEN:
			return optionFormats.opaque;
		case URI_QUERY:
			return optionFormats.string;
		default:
			return optionFormats.error;
		}
	}
	
	public int getContentFormat() {
		return hasContentFormat() ? content_format : MediaTypeRegistery.UNDEFINED;
	}

	/**
	 * Checks if the Content-Format option is present.
	 * @return true if present
	 */
	public boolean hasContentFormat() {
		return content_format != null;
	}
	/**Functions to check whether  option is critical or elective, as determined by the Option Number
	 * Whether an option is elective or critical is entirely(5.4.6)
	 * determined by whether its option number is even or odd.
	 * return True if the option is elective
	 * 
	 * @param optionNumber
	 * @return
	 */
	public static boolean isElective(int optionNumber) {
		return (optionNumber & 1) == 0;
	}
	
	//True if the option is critical
	public static boolean isCritical(int optionNumber) {
		return (optionNumber & 1) == 1;
	}
	
	/**section 5.4.2. Proxy Unsafe or Safe-to-Forward and NoCacheKey
	 * Checks whether an option is unsafe.
	 * @param optionNumber
	 * The option number to check
	 * @return {@code true} if the option is unsafe
	 */
	public static boolean isUnsafe(int optionNumber) {
		// When bit 6 is 1, an option is Unsafe
		return (optionNumber & 2) > 0;
	}
	
	/** Checks whether an option is safe.
	 * @param optionNumber
	 * The option number to check
	 * @return {@code true} if the option is safe
	 */
	public static boolean isSafe(int optionNumber) {
		return !isUnsafe(optionNumber);
	}
	
	/**
	 * Checks whether an option is not a cache-key.
	 * @param optionNumber
	 * The option number to check
	 * @return {@code true} if the option is not a cache-key
	 */
	public static boolean isNoCacheKey(int optionNumber) {
		/*
		 * When an option is not Unsafe, it is not a Cache-Key (NoCacheKey) if
		 * and only if bits 3-5 are all set to 1; all other bit combinations
		 * mean that it indeed is a Cache-Key(section 5.4.6. Option Numbers)
		 */
		return (optionNumber & 0x1E) == 0x1C;
	}
	
	/**
	 * Checks whether an option is a cache-key.
	 * @param optionNumber
	 * The option number to check
	 * @return {@code true} if the option is a cache-key
	 */
	public static boolean isCacheKey(int optionNumber) {
		return !isNoCacheKey(optionNumber);
	}

}
