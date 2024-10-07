package com.vinsys.diam.samples.callback;

import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

@Configuration
public class WSConfig {
	@Bean
	public Wss4jSecurityInterceptor inSecurityInterceptor() {
		Wss4jSecurityInterceptor security = new Wss4jSecurityInterceptor();
		security.setSecurementActions(WSHandlerConstants.ENCRYPTION + " " + WSHandlerConstants.SIGNATURE);
		return security;
	}

	@Bean
	public Wss4jSecurityInterceptor outSecurityInterceptor() {
		Wss4jSecurityInterceptor security = new Wss4jSecurityInterceptor();
		security.setSecurementActions(WSHandlerConstants.SIGNATURE + " " + WSHandlerConstants.ENCRYPTION);
		return security;
	}

}
