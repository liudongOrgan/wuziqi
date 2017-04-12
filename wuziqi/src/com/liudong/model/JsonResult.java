package com.liudong.model;

public class JsonResult<T> {
	public final static String FAIL = "fail";
	public final static String SUCC = "success";

	private String url;
	private String status;
	private T content;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
