package com.ymsl.viralnova.content;

public class Text extends Content {
	
	private String text;
	

	public Text(String text) {
		super("text");
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
