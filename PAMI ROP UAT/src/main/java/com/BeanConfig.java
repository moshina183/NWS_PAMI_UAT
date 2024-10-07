package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class BeanConfig {
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.vinsys.diam.stub.company");
		return marshaller;
	}

//	@Bean
//	public TestWebSevice soapConnector(Jaxb2Marshaller marshaller) {
//		TestWebSevice client = new TestWebSevice();
//		client.setDefaultUri("http://VIN-WTC-TP066:8089/mockCMAddPayEvtSoapBinding");
//		client.setMarshaller(marshaller);
//		client.setUnmarshaller(marshaller);
//		return client;
//	}
}