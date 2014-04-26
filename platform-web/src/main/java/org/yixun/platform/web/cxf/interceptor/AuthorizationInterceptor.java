package org.yixun.platform.web.cxf.interceptor;

import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;

public class AuthorizationInterceptor extends AbstractOutDatabindingInterceptor {

	public AuthorizationInterceptor(String phase) {
		super(phase);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		// TODO Auto-generated method stub
		
	}

}
