package com.vinsys.diam.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;

public class CachedBodyServletInputStream extends javax.servlet.ServletInputStream {

	 private InputStream cachedBodyInputStream;
	 
	 public CachedBodyServletInputStream(byte[] cachedBody) {
	        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
	    }
	
	@Override
	public boolean isFinished() {
		//return cachedBody.available() == 0;
		return false;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int read() throws IOException {
		return cachedBodyInputStream.read();
	}

}
