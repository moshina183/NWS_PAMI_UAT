package com.vinsys.diam.esb;

import org.apache.cxf.annotations.Policies;
import org.apache.cxf.annotations.Policy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "com" })
//@PropertySource(value = "file:${PAWESBCONFIG}")
//@Policies({ @Policy(uri = "classpath:policy.xml", includeInWSDL = true) })
//@EnableScheduling
public class DemoApplication {
	public static void main(String[] args) 
	{
		
		//System.setProperty("javax.net.ssl.trustStore", "D:\\diamwallet\\keystore.jks");
		//System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		String trustStorePathNew = System.getProperty("javax.net.ssl.trustStore");
        String trustStorePasswordNew = System.getProperty("javax.net.ssl.trustStorePassword");
        System.out.println("---------- Trust Store Path New---- : " + trustStorePathNew);
        System.out.println("---------- Trust Store Password New ----: " + trustStorePasswordNew);
        

		//System.setProperty("SIGN_LOCATION", "C:/DM_2024_07_30/config/sign.properties");
		//System.setProperty("ENCRYPT_LOCATION", "C:/DM_2024_07_30/config/encrypt.properties");
		//System.setProperty("DECRYPT_LOCATION", "C:/DM_2024_07_30/config/decrypt.properties");
		//System.setProperty("VERIFY_LOCATION", "C:/DM_2024_07_30/config/verify.properties");

        System.out.println("-SIGN_LOCATION:- : " + System.getProperty("SIGN_LOCATION"));
		System.out.println("-ENCRYPT_LOCATION:- : " + System.getProperty("ENCRYPT_LOCATION"));
		System.out.println("-DECRYPT_LOCATION:- : " + System.getProperty("DECRYPT_LOCATION"));
		System.out.println("-VERIFY_LOCATION:- : " + System.getProperty("VERIFY_LOCATION"));
		
		SpringApplication.run(DemoApplication.class, args);
	}

	
}
