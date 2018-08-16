package org.hvl.CoAP;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hvl.CoAPClient.Request;




public class MessageFormat {
	//As per section 3 Message Format
	
	// number of bits used for the encoding of the CoAP version field
	public static final int VERSION_BITS     = 2;
	
	/** The token, a 0-8 byte array.  9-15 are reserved */
	private byte[] token;
	
	// Bits used for the encoding of the option count field
	public static final int OPTIONCOUNT_BITS = 4;
	
	// Bits used for the encoding of the message type field
	public static final int TYPE_BIT        = 2;
	
	// Bits used for the encoding of the request method/response code field
	public static final int CODE_BITS        = 8;
	
	// Bits used for the encoding of the transaction ID/msg ID
	public static final int ID_BITS         = 16;
	
	// Bits used for the encoding of the option delta
	public static final int OPTIONDELTA_BITS = 4;
	
	/** Bits used for the encoding of the base option length field
	*if all bits in this field are set to one, the extended option length
	**field is additionally used to encode the option length */
		public static final int OPTIONLENGTH_BASE_BITS     = 4;
		
	/** number of bits used for the encoding of the extended option length field
	*this field is used when all bits in the base option length field 
	** are set to one */
		public static final int OPTIONLENGTH_EXTENDED_BITS = 8;
		
		/** The charset for CoAP is UTF-8 */
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	//The message's ID
	
	private int MID = -1;
	
	/** options of this message*/
	private CoAPOptionRegistry options;
	
	//The message's code
	private int code;
	
	/** The type of the message. One of {CON, NON, ACK or RST}. */
	private CoAPCodeRegistries.Type Type;
	
	int type=0;
	
	//The message's URI
	private URI uri;
	
	/*
	 * The message's version. Default message's version set to 1. Other numbers are reserved
	 * for future versions
	 */
	private int version = 1;
	
	//The payload of message's 
	private byte[] payload;
	
	private boolean complete;
	
	//A time stamp of the messages
		private long timestamp;

	//The message's options
		private Map<Integer, List<Options>> optionMap = new TreeMap<Integer, List<Options>>();
	
		public int getVersion() {
			return this.version;
		}

	
	public MessageFormat newReply(boolean ack) {

		MessageFormat reply = new MessageFormat();
		
		/** set message type CON<NON<ACK<RST
		**return type for CON message can be either ACK or RST(page 8) 
		if no packets are lost, each CON message 
		*return message of type ACK or RST 
		*/
		if (Type == CoAPCodeRegistries.Type.CON) {
			reply.Type = ack ? 
					CoAPCodeRegistries.Type.ACK : CoAPCodeRegistries.Type.RST;
		} else {
			reply.Type = CoAPCodeRegistries.Type.NON;
		}
		
		//prints the message ID
		reply.MID = this.MID;
		
		 //prints token
		//Section 5.3
		reply.setOption(getFirstOption(CoAPOptionRegistry.TOKEN));;
		
		// set the receiver URI of the reply to the sender of this message
		reply.uri = this.uri;
		
		 //create an empty reply by default
		reply.code = CoAPCodeRegistries.EMPTY_MESSAGE;
		
		return reply;
		
	}
	/*
	 * This method sets the payload of this CoAP message
	 * 
	 * @param payload The payload to which the current message payload should be
	 *  
	 */
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	
	public void setPayload(String payload, int mediaType) {
		if (payload != null) {
			try {
				// set internal byte array
				setPayload(payload.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			}
			
			// set content type option
			setOption(new Options(mediaType, CoAPOptionRegistry.CONTENT_TYPE));
		}
	}
	
	public void setPayload(String payload) {
		setPayload(payload, MediaTypeRegistery.PLAIN);
	}
	
	/*
	 * Sets the option with the specified option number
	 * 
	 * @param opt The option to set
	 */
	public void setOption(Options opt) {

		if (opt != null) {
			List<Options> options = new ArrayList<Options>();
			options.add(opt);
			setOptions(opt.getOptionNumber(), options);
		}
	}
	
	public boolean hasOption(int optionNumber) {
		return getFirstOption(optionNumber) != null;
	}
	
	
	/**
	 * Get the size of the payload. 
	 * if payload is not specified return null otherwise return length of payload
	 * @return the payload size
	 */
	public int getpayloadSize() {
		//return payload == null ? 0 : payload.length;
        if(payload == null)
        	return 0;
        else 
        	return payload.length;
	}
	
	/**
	 * Returns the payload in the form of a string. 
	 * Returns blank string if no
	 * payload is defined.
	 */
	public String getPayloadString() {
		if (payload==null)
			return "";
		return new String(payload, UTF8_CHARSET);
	}
	
	
	public void setOptions(int optionNumber, List<Options> opt) {
		// TODO Check if all options are consistent with optionNumber
		optionMap.put(optionNumber, opt);
	}

	/*
	 * Returns the first option with the specified option number
	 */
	public Options getFirstOption(int optionNumber) {
  		
  		List<Options> list = getOptions(optionNumber);
  		return list != null && !list.isEmpty() ? list.get(0) : null;
  	    //if(list != null && !list.isEmpty())
  	    	//list.get(0);
  	    //else
  	    	//return null;
	
	}
      
      public void addOption(Options opt) {
  		
  		List<Options> list = optionMap.get(opt.getOptionNumber());
  		if (list == null) {
  			list = new ArrayList<Options>();
  			optionMap.put(opt.getOptionNumber(), list);
  		}
  		list.add(opt);
  	}	
      
      
      /**
  	 * This method gets the set of options. If no set has defined,a new
  	 * one will be created. EmptyMessages will not have any options.
  	 * 
  	 * @return the options
  	 */
  	public CoAPOptionRegistry getOptions() {
  		if (options == null)
  			options = new CoAPOptionRegistry();
  		return options;
  	}
      
      public List<Options> getOptions(int optionNumber) {
  		return optionMap.get(optionNumber);
  	}
      
      public boolean hasFormat(int mediaType) {
  		Options opt = getFirstOption(CoAPOptionRegistry.CONTENT_TYPE);
  		//return opt != null ? opt.getIntValue() == mediaType : false;
  	    if(opt != null)
  	    	return opt.getIntValue() == mediaType;
  	    else 
  	    	return false;
      }

	public static MessageFormat newAcknowledgement(MessageFormat msg) {
		
		MessageFormat ack = new MessageFormat();
		
		// set message type to Acknowledgement
		ack.setType(CoAPCodeRegistries.Type.ACK);
		
		// echo the Message ID(4.2)
		ack.setID(msg.getID());
		
		// set receiver URI to sender URI of the message
		// to acknowledge
		ack.setURI(msg.getURI());
		
		// create an empty Acknowledgement by default,can be piggy-backed with a response by the user
		ack.setCode(CoAPCodeRegistries.EMPTY_MESSAGE);
		
		return ack;
	}
     
      public static MessageFormat newReset(MessageFormat msg) {
  		
  		MessageFormat rst = new MessageFormat();
  		
  		// set message type to Reset
  		rst.setType(CoAPCodeRegistries.Type.RST);
  		
  		// echo the Message ID
  		rst.setID(msg.getID());
  		
  		// set receiver URI to sender URI of the message to reset
  		rst.setURI(msg.getURI());
  		
  		// Reset must be empty(page 8)
  		//Empty Message
  		 //A message with a Code of 0.00; neither a request nor a response.
  		rst.setCode(CoAPCodeRegistries.EMPTY_MESSAGE);
  		
  		return rst;
  	}
  	
      public Request setURI(String uri) {
  		try {
  			if (!uri.startsWith("coap://") && !uri.startsWith("coaps://"))
  				uri = "coap://" + uri;
  			
  			setURI(new URI(uri));
  		 return (Request) this;
  		} catch (URISyntaxException e) {
  			System.out.printf("[%s] Failed to set URI: %s\n",
  				getClass().getName(), e.getMessage());
  			return (Request) this;
  		}
  	}
      

      
      public void setURI(URI uri2) {
		// TODO Auto-generated method stub
		
	}
      

      /*
  	 * This function returns the ID of this CoAP message
  	 * 
  	 * @return The current ID.
  	 */  
    
	protected int getID() {
    	  return this.MID;
	}
      
	/*
	 * This function returns the URI of this CoAP message
	 * 
	 * @return The current URI
	 */  
	public URI getURI() {
  		return this.uri;
  	}
     
	/*
	 * Default constructor for a new CoAP message
	 */
	public MessageFormat () {
  	}
      
	/*
	 * Constructor for a new CoAP message
	 * 
	 * @param type The type of the CoAP message ACK,NON,RST,CON
	 * @param code The code of the CoAP message GET,PUT,POST,DELETE
	 */  
	public MessageFormat(CoAPCodeRegistries.Type type, int code) {
  		this.Type = type;
  		this.code = code;
  	}
      
	/*
	 * Constructor for a new CoAP message
	 * 
	 * @param uri The URI of the CoAP message
	 * @param payload The payload of the CoAP message
	 */
      public MessageFormat(URI uri, CoAPCodeRegistries.Type type, int code, int id, byte[] payload) {
  		this.uri = uri;
  		this.Type = type;
  		this.code = code;
  		this.MID = id;
  		this.payload = payload;
  	}
      
      /*This procedure sets the ID of this CoAP message
  	 * 
  	 * @param id The message ID to which the current message ID should
  	 *           be set to
  	 */
      public int setID(int id) {
  		return(this.MID = id);
  	}
    
      /*
  	 * This methods sets the code of this CoAP message
  	 * to GET,PUT,POST or DELETE depending on argument passed
  	 * @param code The message code to which the current message code should
  	 *             be set to
  	 */
      public void setCode(int code) {
  		this.code = code;
  	}
      
      public int getCode() {
  		return this.code;
  	}
      /*
  	 * This procedure sets the type of this CoAP message
  	 * 
  	 * @param Type The message type to which the current message type should
  	 *                be set to
  	 */
      public void setType(CoAPCodeRegistries.Type ack) {
  		this.Type = ack;
  	}
      
      /*
  	 * This function returns the type of this CoAP message
  	 * Confirmable (0), Non-confirmable (1), Acknowledgement (2), or
     * Reset (3)
  	 * @return The current type.
  	 */
  	public CoAPCodeRegistries.Type getType() {
  		return Type;
  	}
      
      public static CoAPCodeRegistries.Type getTypeByID(int id) {
  		switch (id) {
  			case 0:
  				return CoAPCodeRegistries.Type.CON;
  			case 1:
  				return CoAPCodeRegistries.Type.NON;
  			case 2:
  				return CoAPCodeRegistries.Type.ACK;
  			case 3:
  				return CoAPCodeRegistries.Type.RST;
  			default:
  				return CoAPCodeRegistries.Type.CON;
  		}
  	}
     
     /*
	 * To checks, if the message is a request message(0)
	 * 
	 * @return True if the message is a request
	 */
	public boolean isRequest() {
		return CoAPCodeRegistries.isRequest(code);
	}
	
	/*
	 * To checks if the message is a response message
	 * 
	 * @return True if the message is a response
	 */
	public boolean isResponse() {
		return CoAPCodeRegistries.isResponse(code);
	}
    
	public boolean isConfirmable() {
		return type == CoAPCodeRegistries.CON;
	}
	
	public boolean isNonConfirmable() {
		
		return type == CoAPCodeRegistries.NON;
	}
	
	public boolean isAcknowledgement() {
		return type == CoAPCodeRegistries.ACK;
	}
	
	public boolean isReset() {
		return type == CoAPCodeRegistries.RST;
	}
	
	public boolean isReply() {
		return isAcknowledgement() || isReset();
	}
	
	public void handleBy(HandelMessage handler) {
		// TODO Auto-generated method stub
		
	}
	
public void PrintDetail(PrintStream out) {
		
		out.println("==[COAP MESSAGE]======================================");
		
		List<Options> options = getOptionList();
		
		out.printf("URI of the Message   : %s\n", uri != null ? uri.toString() : "NULL");
		out.printf("ID of the Message   : %d\n", MID);
		out.printf("Code of the Message  : %s\n", CoAPCodeRegistries.toString(code));
		out.printf("Options of the Message: %d\n", options.size());
		for (Options opt : options) {
			out.printf("  * %s: %s (%d Bytes)\n", 
				opt.getName(), opt.getDisplayValue(), opt.getLength()
			);
		}
		out.printf("Payload: %d Bytes\n", getpayloadSize());
		out.println("------------------------------------------------------");
		if (getpayloadSize() > 0) out.println(getPayloadString());
		out.println("======================================================");
		
	}
	
	private List<Options> getOptionList() {

      List<Options> list = new ArrayList<Options>();
		
		for (List<Options> option : optionMap.values()) {
			list.addAll(option);
		}
		
		return list;
	}	

	public void Printlog() {
		PrintDetail(System.out);
	}

	/*
	 * Notification method that is called when the message's complete flag
	 * changed to true.
	 * 
	 Subclasses may override this method to add custom handling code*/
	 
	protected void completed() {
		// do nothing
	}
	
	
	
	/*protected boolean iscompleted() {
		return complete;
		
	}*/
	/*check for the empty token, if no token specified returns null or token length is zero 
	 * 
	 */
	public boolean EmptyToken() {
		return token == null || token.length == 0;
	}
	
	/** This method returns Token of the message
	 * 
	 */
	public byte[] getToken() {
		return token;
	}
	
	public void timedOut() {
		System.out.println("Time out");
	}
	
	/*
	 * check two messages if they have the same message ID
	 * 
	 * @param msg1 The first message
	 * @param msg2 the second message
	 * @return True iif the messages are same
	 */
	public static boolean isDuplicateMessageID(MessageFormat msg1, MessageFormat msg2) 
	{
		
		if (
			msg1 != null && msg2 != null &&  // both messages must exist
		                  msg1.getID() == msg2.getID()     // checks for the same IDs
		) 
			
		return true;
			
	    else {
			return false;
		}
	}
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* This method 
	 * sets the timestamp for current message.
	 * 
	 * @param timestamp, in milliseconds
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		
		
	}
	
	
	
	
	/*
	 * get the timestamp associated with this message.
	 * 
	 * @return The timestamp , in milliseconds
	 */
	public long getTimestamp() {
		return timestamp;
	}
     
	public String endpointID() {
		InetAddress address = null;
		try {
			address = getInetAddress();
		} catch (UnknownHostException e) {
		}
		return String.format("%s:%d", 
			address != null ? address.getHostAddress() : "NULL",
			uri != null ? uri.getPort() : -1
		);
	}
	
	public InetAddress getInetAddress() throws UnknownHostException {
		return InetAddress.getByName(uri != null ? uri.getHost() : null);
	}
	
	/*identifier of the sender
	 * 
	 */
	public String key() {
		return String.format("%s#%d", endpointID(),MID);
	}


	
	
	public void setContentType(MediaTypeRegistery mediaType) {}
}
