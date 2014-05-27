package com.ymsl.viralnova.content;

public abstract class Content {
	
	protected String type;
	
	public Content(String type) {
		super();
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
