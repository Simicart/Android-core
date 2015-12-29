package com.simicart.core.base.network.mock;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

public class MockStatusLine implements StatusLine{
	
	protected int status_code;
	protected String reason;
	
	public void setStatusCode(int status)
	{
		status_code = status;
	}
	

	@Override
	public ProtocolVersion getProtocolVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReasonPhrase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
