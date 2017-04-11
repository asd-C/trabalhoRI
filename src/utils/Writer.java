package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Writer {
	public static final String separator_url_content = Reader.separator_url_content;
	public static final String separator_document = Reader.separator_document;
	private static final String dir = "output/";
	
	public boolean save(HashMap<String, String> contents, String domain) {
		domain = domain.replace("https://", "");
		domain = domain.replace("http://", "");
		return saveTo(format(contents), dir + domain);
	}
	
	public String format(HashMap<String, String> contents) {
		String text = "";
		Entry<String, String> tmp;
		Iterator<Entry<String, String>> it = contents.entrySet().iterator();
		
		while (it.hasNext()) {
			tmp = it.next();
			text += (tmp.getKey() + separator_url_content + tmp.getValue() + separator_document);
		}
		
		return text;
	}
	
	public boolean saveTo(String content, String filename) {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File(filename), true));) {
			pw.print(content);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
