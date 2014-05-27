package com.ymsl.viralnova.content;

public class Image extends Content {
	
	private String url;
	private String pre;
	
	public Image(String url, String pre) {
		super("image");
		this.url = url;
		this.pre = pre;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPre() {
		return pre;
	}
	public void setPre(String pre) {
		this.pre = pre;
	}
	
}
