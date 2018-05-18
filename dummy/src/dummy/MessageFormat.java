package dummy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MessageFormat {
	//As per section 3 Message Format
	
	// number of bits used for the encoding of the CoAP version field
	public static final int VERSION_BITS     = 2;
	
	/** The token, a 0-8 byte array. */
	private byte[] token;
	
	// number of bits used for the encoding of the option count field
	public static final int OPTIONCOUNT_BITS = 4;
	
	// number of bits used for the encoding of the message type field
	public static final int TYPE_BIT        = 2;
	
	// number of bits used for the encoding of the request method/response code field
	public static final int CODE_BITS        = 8;
	
	// number of bits used for the encoding of the transaction ID/msg ID
	public static final int ID_BITS         = 16;
	
	// number of bits used for the encoding of the option delta
	public static final int OPTIONDELTA_BITS = 4;
	
	// number of bits used for the encoding of the base option length field
	// if all bits in this field are set to one, the extended option length
	// field is additionally used to encode the option length
		public static final int OPTIONLENGTH_BASE_BITS     = 4;
		
	// number of bits used for the encoding of the extended option length field
	// this field is used when all bits in the base option length field 
	// are set to one
		public static final int OPTIONLENGTH_EXTENDED_BITS = 8;
	
	//The message's ID
	private int messageID = -1;
	
	// checks for duplicate of messages are if they have the same message ID
		//private MessageFormat duplicate;
	
	//The message's code
	private int code;
	
	/** The type. One of {CON, NON, ACK or RST}. */
	private messageType.Type Type;
	
	//The message's URI
	private URI uri;
	
	/*
	 * The message's version. This must be set to 1. Other numbers are reserved
	 * for future versions
	 */
	private int version = 1;
	
	//The message's payload
	private byte[] payload;
	
	private boolean complete;

	//The message's options
		private Map<Integer, List<Options>> optionMap = new TreeMap<Integer, List<Options>>();
	


	
	public MessageFormat newReply(boolean ack) {

		MessageFormat reply = new MessageFormat();
		
		/* set message type CON<NON<ACK<RST
		**return type for confirmable message can be either ACK or RST(page 8) 
		*When no packets are lost, each Confirmable message elicits exactly 
		*one return message of type Acknowledgement or type Reset 
		*/
		if (Type == messageType.Type.CON) {
			reply.Type = ack ? 
					messageType.Type.ACK : messageType.Type.RST;
		} else {
			reply.Type = messageType.Type.NON;
		}
		
		//echo the message ID
		reply.messageID = this.messageID;
		
		 //echo token
		//Section 5.3
		reply.setOption(getFirstOption(CoAP_Opt_num_Registry.TOKEN));;
		
		// set the receiver URI of the reply to the sender of this message
		reply.uri = this.uri;
		
		 //create an empty reply by default
		reply.code = messageType.EMPTY_MESSAGE;
		
		return reply;
		
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
	
      
	public void setOptions(int optionNumber, List<Options> opt) {
		// TODO Check if all options are consistent with optionNumber
		optionMap.put(optionNumber, opt);
	}

	public Options getFirstOption(int optionNumber) {
  		
  		List<Options> list = getOptions(optionNumber);
  		return list != null && !list.isEmpty() ? list.get(0) : null;
  	}
      
      public void addOption(Options opt) {
  		
  		List<Options> list = optionMap.get(opt.getOptionNumber());
  		if (list == null) {
  			list = new ArrayList<Options>();
  			optionMap.put(opt.getOptionNumber(), list);
  		}
  		list.add(opt);
  	}	
      
      public List<Options> getOptions(int optionNumber) {
  		return optionMap.get(optionNumber);
  	}

	public static MessageFormat newAcknowledgement(MessageFormat msg) {
		
		MessageFormat ack = new MessageFormat();
		
		// set message type to Acknowledgement
		ack.setType(messageType.Type.ACK);
		
		// echo the Message ID(4.2)
		ack.setID(msg.getID());
		
		// set receiver URI to sender URI of the message
		// to acknowledge
		ack.setURI(msg.getURI());
		
		// create an empty Acknowledgement by default,can be piggy-backed with a response by the user
		ack.setCode(messageType.EMPTY_MESSAGE);
		
		return ack;
	}
     
      public static MessageFormat newReset(MessageFormat msg) {
  		
  		MessageFormat rst = new MessageFormat();
  		
  		// set message type to Reset
  		rst.setType(messageType.Type.RST);
  		
  		// echo the Message ID
  		rst.setID(msg.getID());
  		
  		// set receiver URI to sender URI of the message to reset
  		rst.setURI(msg.getURI());
  		
  		// Reset must be empty(page 8)
  		//Empty Message
  		 //A message with a Code of 0.00; neither a request nor a response.
  		rst.setCode(messageType.EMPTY_MESSAGE);
  		
  		return rst;
  	}
  	
      public boolean setURI(String uri) {
  		try {
  			setURI(new URI(uri));
  			return true;
  		} catch (URISyntaxException e) {
  			System.out.printf("[%s] Failed to set URI: %s\n",
  				getClass().getName(), e.getMessage());
  			return false;
  		}
  	}
      

      
      protected void setURI(URI uri2) {
		// TODO Auto-generated method stub
		
	}

      /*
  	 * This function returns the ID of this CoAP message
  	 * 
  	 * @return The current ID.
  	 */  
    
	protected int getID() {
    	  return this.messageID;
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
	public MessageFormat(messageType.Type type, int Code) {
  		this.Type = type;
  		this.code = Code;
  	}
      
	/*
	 * Constructor for a new CoAP message
	 * 
	 * @param uri The URI of the CoAP message
	 * @param payload The payload of the CoAP message
	 */
      public MessageFormat(URI uri, messageType.Type type, int code, int id, byte[] payload) {
  		this.uri = uri;
  		this.Type = type;
  		this.code = code;
  		this.messageID = id;
  		this.payload = payload;
  	}
      
      /*This procedure sets the ID of this CoAP message
  	 * 
  	 * @param id The message ID to which the current message ID should
  	 *           be set to
  	 */
      public void setID(int id) {
  		this.messageID = id;
  	}
    
      /*
  	 * This procedure sets the code of this CoAP message
  	 * 
  	 * @param code The message code to which the current message code should
  	 *             be set to
  	 */
      public void setCode(int code) {
  		this.code = code;
  	}
      /*
  	 * This procedure sets the type of this CoAP message
  	 * 
  	 * @param msgType The message type to which the current message type should
  	 *                be set to
  	 */
      public void setType(messageType.Type ack) {
  		this.Type = ack;
  	}
      
      /*public static MessageFormat fromByteArray(byte[] byteArray) {

  		//Initialize DatagramReader
  		DatagramReader datagram = new DatagramReader(byteArray);
  		
  		//Read current version
  		int version = datagram.read(VERSION);
  		
  		//Read current type
  		messageType.Type type = getTypeByID(datagram.read(TYPE_BIT));
  		
  		//Read number of options
  		int optionCount = datagram.read(OPTIONCOUNT_BITS);
  		
  		//Read code
  		int code = datagram.read(CODE_BITS);
  		if (!messageType.isValid(code)) {
  			System.out.printf("ERROR: Invalid message code: %d\n", code);
  			return null;
  		}

  		// create new message with subtype according to code number
  		MessageFormat msg;
  		try {
  			msg = messageType.Code.newInstance();
  		} catch (InstantiationException e) {
  			e.printStackTrace();
  			return null;
  		} catch (IllegalAccessException e) {
  			e.printStackTrace();
  			return null;
  		}
  		msg.version = version;
  		msg.Type = type;
  		msg.code = code;
  		
  		//Read message ID
  		msg.messageID = datagram.read(ID_BITS);
  		return msg;
}*/

     public static messageType.Type getTypeByID(int id) {
  		switch (id) {
  			case 0:
  				return messageType.Type.CON;
  			case 1:
  				return messageType.Type.NON;
  			case 2:
  				return messageType.Type.ACK;
  			case 3:
  				return messageType.Type.RST;
  			default:
  				return messageType.Type.CON;
  		}
  	}

	public void handleBy(HandlMessage handler) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Notification method that is called when the message's complete flag
	 * changed to true.
	 * 
	 * Subclasses may override this method to add custom handling code.
	 */
	protected void completed() {
		// do nothing
	}
	
	
	protected boolean iscompleted() {
		return complete;
		
	}
	/*check for the empty token
	 * 
	 */
	public boolean hasEmptyToken() {
		return token == null || token.length == 0;
	}
	
	/**
	 * Gets the 0--8 byte token.
	 *@return the token
	 */
	public byte[] getToken() {
		return token;
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
     
}
