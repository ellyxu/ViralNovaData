package com.ymsl.viralnova.content;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ymsl.viralnova.JdbcUtils;

public class CopyOfContentMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(
					"C:\\Users\\006\\workspace\\ViralNovaData\\src\\OneArticle.txt");
			br = new BufferedReader(fileReader);
			
			String line = br.readLine();
			String url = StringUtils.substringAfter(line, "url = ").trim();
			System.out.println(url);
			
			line = br.readLine();
			
			List<Content> contents = new ArrayList<Content>();
			line = fillContents(br, line, contents);
			
			String string = JSON.toJSONString(contents);
			string = "{list: " + string + "}";
			System.out.println(string);
			
			
			Connection conn = null;
			java.sql.PreparedStatement ps = null;
			try {
				conn = JdbcUtils.getConnection();
//				String sql = "INSERT INTO articles (title,url,date,category,imageUrl,entryContent) VALUES (?,?,?,?,?,?)";
				String sql = "UPDATE articles SET content=? WHERE url=?";
				ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, string);
				ps.setString(2, url);
				int executeUpdate = ps.executeUpdate();
				System.out.println("executeUpdate = " + executeUpdate);
				
			} catch (Exception e) {
				throw new RuntimeException("数据库连接出错了");
			} finally {
				JdbcUtils.release(null, ps, conn);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private static String fillContents(BufferedReader br, String line,
			List<Content> contents) throws IOException {
		while (line != null) {
			if (line.trim().startsWith("<p>")) {
				contents.add(new Text(StringUtils.substringBetween(line, "<p>", "</p>")));
			} else if(line.contains("wp-caption-text")){
				String pre = br.readLine();
				while((line = br.readLine()) != null){
					if (line.contains("gallery-icon")) {
						line = addImage(br, contents, pre);
						break;
					}
				}
			}
			else if (line.contains("gallery-icon")) {
				String pre = null;
				line = addImage(br, contents, pre);
			}
			line = br.readLine();
		}
		return line;
	}

	private static String addImage(BufferedReader br, List<Content> contents,
			String pre) throws IOException {
		String line;
		line = br.readLine();
		String imageUrl = parseImageUrl(line);
		contents.add(new Image(imageUrl, pre));
		return line;
	}
	
	private static String parseImageUrl(String line) {
		String regexImageUrl = "(src=\"http).+\\.[a-z]{3,5}\"";
		Pattern patternImageUrl = Pattern
				.compile(regexImageUrl);
		Matcher matcherImageUrl = patternImageUrl.matcher(line);
		if (matcherImageUrl.find()) {
			String group = matcherImageUrl.group();
			String imageUrl = group.substring(5,
					group.length() - 1);
			return imageUrl;
		}
		return null;
	}

}
