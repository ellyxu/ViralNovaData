package com.ymsl.viralnova.content;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ContentMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Content> contents = new ArrayList<Content>();
		
		contents.add(new Text("Humans, in general, have an obsession with the future. We worry about what we will be doing in 5 years, what technological advancements will be and what the world will look like for our children. We have always been that way."));
		contents.add(new Image("http://content3.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future.jpg", "1.) They thought that THIS is what voicemail would look like."));
		contents.add(new Image("http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future2.jpg", "2.) Instead of a portable AC unit, maybe just air condition the entire vehicle?"));
		
		String string = JSON.toJSONString(contents);
		System.out.println(string);
	}

}
