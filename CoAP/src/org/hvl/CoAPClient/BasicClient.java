package org.hvl.CoAPClient;


import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hvl.CoAP.CoAPCodeRegistries;
import org.hvl.CoAP.MediaTypeRegistery;
import org.hvl.CoAPServer.Response;

public class BasicClient {
	
	
	    private static final int PORT = CoAPClient.DEFAULT_PORT;
	    static int counter = 0;
	    CoapChannelManager channelManager = null;
	    CoapClientChannel clientChannel = null;
	    public static void main(String[] args)  {
	        System.out.println("Start CoAP Client");
	        
	        //CommandLineParser cmdParser = new GnuParser();
	        CommandLineParser parser = new DefaultParser();
	        Options options = new Options();
	         System.out.println("Select options-----------------");
	        options.addOption("t", true, "Content type for given resource for PUT/POST");
	        options.addOption("e", true, "Include text as payload");
	        options.addOption("m", true, "Request method (get|put|post|delete), default is 'get'");
	        options.addOption("N", false, "Send NON-confirmable message");
	        options.addOption("T", true, "Include specified token");
	        CommandLine cmd = null;
	        
	        //String values[] = cmd.getOptionValues("t");
	        //System.out.println(Arrays.asList(values));
	        try  {
	        	//args = "coap://[::1]/.well-known/core";
	            cmd = parser.parse(options, args);
	            //System.out.println(cmd);
	        } catch (ParseException  e) {
	            System.out.println("Unexpected exception: " + e.getMessage() );
	            showHelp(options);
	        }
	        
	        // Make sure we have at least the URI argument
	        if (cmd == null || cmd.getArgs().length != 1) {
	            showHelp(options);
	        }

	        BasicClient client = new BasicClient();
	        
	        //client.channelManager = BasicCoapChannelManager.getInstance();
	        client.runTestClient(cmd);
	    }

	    public static void showHelp(Options options) {
	            HelpFormatter formatter = new HelpFormatter();
	            formatter.printHelp("CoAP-Client URI", "", options,
	                    "Examples: \n"
	                    + "    CoAP-Client -m get coap://[::1]/.well-known/core\n");
	            System.exit(-1);
	    }

	    public void runTestClient(CommandLine cmd){
	        try {
	            String urlOrig = cmd.getArgs()[0];
	            
	            urlOrig = urlOrig.replaceFirst("coap", "http");
	            URL url = new URL(urlOrig);
	            int port = url.getPort();
	            if (port < 0) port = PORT;

	            clientChannel = channelManager.connect(this, InetAddress.getByName(url.getHost()), port);
	            Request request;

	            boolean confirm = !cmd.hasOption("N");
	            switch (cmd.getOptionValue("m", "get").toLowerCase()) {
	                case "put":
	                    request = clientChannel.createRequest(confirm, CoAPCodeRegistries.PUT);
	                    break;
	                case "post":
	                  request = clientChannel.createRequest(confirm, CoAPCodeRegistries.POST);
	                    break;
	                case "delete":
	                   request = clientChannel.createRequest(confirm, CoAPCodeRegistries.DELETE);
	                    break;
	                default:
	                    request = clientChannel.createRequest(confirm, CoAPCodeRegistries.GET);
	            }

	            if (cmd.hasOption("e")) request.setPayload(cmd.getOptionValue("e"));
	            if (cmd.hasOption("T")) request.setToken(cmd.getOptionValue("T").getBytes());
	            if (cmd.hasOption("P")) request.setURI(cmd.getOptionValue("P"));
	            if (cmd.hasOption("t")) {
	                Integer type = Integer.parseInt(cmd.getOptionValue("t"));
	                request.setContentType(MediaTypeRegistery.parse(type));
	            }

	            //request.setUriHost(url.getHost());
	            request.setURI("coap://example.net/");
	            request.getCode();
    
	            //request.setUriPath(url.getPath());
	            //request.setUriQuery(url.getQuery());
	          //request.setURI("/.well-known/core");
	          //request.setUriPath("/st");
//				coapRequest.setContentType(CoapMediaType.octet_stream);
//				coapRequest.setToken("ABCD".getBytes());
//				coapRequest.setUriHost("123.123.123.123");
//				coapRequest.setUriPort(1234);
//				coapRequest.setUriPath("/sub1/sub2/sub3/");
//				coapRequest.setUriQuery("a=1&b=2&c=3");
//				coapRequest.setProxyUri("http://proxy.org:1234/proxytest");

				clientChannel.sendMessage(request);
				System.out.println("Sent Request");
	        } catch (UnknownHostException e) {
	            Logger.getLogger(BasicClient.class.getName()).log(Level.SEVERE, null, e);
	            e.printStackTrace();
	        } catch (MalformedURLException ex) {
	            Logger.getLogger(BasicClient.class.getName()).log(Level.SEVERE, null, ex);
	            System.exit(-2);
	        }
	    }

		public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
			System.out.println("Connection Failed");
		}

		public void onResponse(CoapClientChannel channel, Response response) {
			System.out.println("Received response");
		}
	}

	


