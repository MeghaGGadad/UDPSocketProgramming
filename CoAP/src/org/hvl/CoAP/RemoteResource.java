package org.hvl.CoAP;

import java.util.StringTokenizer;



public class RemoteResource {

	
	
	public static RemoteResource newRoot(String linkFormat) {
		RemoteResource resource = new RemoteResource();
		resource.setResourceIdentifier("");
		resource.setResourceName("root");
		//resource.addLinkFormat(linkFormat);
		return resource;
	}

	

	private String resourceName;

	
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	private String resourceIdentifier;

	private void setResourceIdentifier( String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
		
		
	}

	public void log() {
		// TODO Auto-generated method stub
		
	}

}
