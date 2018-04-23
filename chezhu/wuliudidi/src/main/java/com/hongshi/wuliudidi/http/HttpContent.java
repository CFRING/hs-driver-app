package com.hongshi.wuliudidi.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;
public class HttpContent {
	
	private String url;
	private byte[] content;
	private int responseCode;
	private long contentLength;
	private Exception exception;
	private Map<String, ? extends Object> params;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public String getContentAsString() {
		return content == null ? null : new String(content);
	}
	
	public String getContentAsString(String charset) {
		try {
			return content==null ? null : new String(content,charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	public long getContentLength() {
		return contentLength;
	}
	
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public Map<String, ? extends Object> getParams() {
		return params;
	}
	public void setParams(Map<String, ? extends Object> params) {
		this.params = params;
	}
	
}
