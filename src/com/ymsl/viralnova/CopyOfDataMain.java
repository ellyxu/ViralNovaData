package com.ymsl.viralnova;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CopyOfDataMain {

	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(
					"C:\\Users\\006\\workspace\\ViralNovaData\\src\\articles.txt");
			br = new BufferedReader(fileReader);
			String line = "";
			line = br.readLine();
			
			List<Article> articles = new ArrayList<Article>();
			Article article = null;
			while (line != null) {
				if (line.trim().equals("<article class=\"article\">")) {
					article = new Article();
				} else if (article != null) {
					parseArticle(line, articles, article);
				}
				line = br.readLine();
			}
			
			for(int i = articles.size()-1; i >= 0; i--){
				article = articles.get(i);
				Connection conn = null;
				java.sql.PreparedStatement ps = null;
				try {
					conn = JdbcUtils.getConnection();
					String sql = "INSERT INTO articles (title,url,date,category,imageUrl,entryContent) VALUES (?,?,?,?,?,?)";
					ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, article.getTitle());
					ps.setString(2, article.getUrl());
					ps.setString(3, article.getDate());
					ps.setString(4, article.getCategory());
					ps.setString(5, article.getImageUrl());
					ps.setString(6, article.getEntryContent());
					int executeUpdate = ps.executeUpdate();
					System.out.println("executeUpdate = " + executeUpdate);
					
				} catch (Exception e) {
					throw new RuntimeException("数据库连接出错了");
				} finally {
					JdbcUtils.release(null, ps, conn);
				}
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

	private static void parseArticle(String line, List<Article> articles,
			Article article) {
		if (line.contains("<h2 class=\"entry-title\">")) {
			parseTitle(line, article);
			parseUrl(line, article);
		} else if (line.contains("<span class=\"thetime\">")
				&& line.contains("<span class=\"thecategories\">")) {
			parseCategory(line, article);
		} else if (line
				.contains("class=\"attachment-featured wp-post-image\"")) {
			parseImageUrl(line, article);
		} else if (line.contains("Read More...</a></p>")) {
			parseEntryContent(line, article);
		} else if (line.trim().equals("</article>")){
			articles.add(article);
			article = null;
		}
	}

	private static void parseEntryContent(String line, Article article) {
		String entryContent = line.substring(0,
				line.indexOf("<")).trim();
		article.setEntryContent(entryContent);
//		System.out.println("entryContent= " + entryContent);
	}

	private static void parseImageUrl(String line, Article article) {
		String regexImageUrl = "(src=\"http).+\\.[a-z]{3,5}\"";
		Pattern patternImageUrl = Pattern
				.compile(regexImageUrl);
		Matcher matcherImageUrl = patternImageUrl.matcher(line);
		if (matcherImageUrl.find()) {
			String group = matcherImageUrl.group();
			String imageUrl = group.substring(5,
					group.length() - 1);
			article.setImageUrl(imageUrl);
//			System.out.println("imageUrl= " + imageUrl);
		}
	}

	private static void parseCategory(String line, Article article) {
		parseDate(line, article);
		String regexCategory = "(Entertainment|Stories|Other Stuff|In the News)";
		Pattern pattenCategory = Pattern.compile(regexCategory);
		Matcher matcherCategory = pattenCategory.matcher(line);
		if (matcherCategory.find()) {
			String category = matcherCategory.group();
			article.setCategory(category);
//			System.out.println("category= " + category);
		}
	}

	private static void parseDate(String line, Article article) {
		String regexTime = "[A-Z][a-z]{2,8} [0-3]?[0-9], (19|20)[0-9]{2}";
		Pattern patternTime = Pattern.compile(regexTime);
		Matcher matcherTime = patternTime.matcher(line);
		if (matcherTime.find()) {
			String date = matcherTime.group();
			article.setDate(date);
//			System.out.println("date= " + date);
		}
	}

	private static void parseUrl(String line, Article article) {
		int index1 = line.indexOf("href");
		int index2 = line.indexOf("\"", index1);
		int index3 = line.indexOf("\"", index2+1);
		String url = line.substring(index2+1, index3);
		article.setUrl(url);
//		System.out.println("url= " + url);
	}

	private static void parseTitle(String line, Article article) {
		String regexTitle = ">[.|[^<]]*</a></h2>";
		Pattern patternTitle = Pattern.compile(regexTitle);
		Matcher matcherTitle = patternTitle.matcher(line);
		if (matcherTitle.find()) {
			String group = matcherTitle.group();
			String title = group.substring(1, group.indexOf("<")).trim();
			article.setTitle(title);
//			System.out.println("title= " + title);
		}
	}

}
