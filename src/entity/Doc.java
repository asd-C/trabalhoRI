package entity;

import java.util.HashMap;

public class Doc {
	String url;
	HashMap<String, Integer> indexs;
	int size;
	
	public Doc() {
		indexs = new HashMap<String, Integer>();  
	}
	
	public Doc(String url, HashMap<String, Integer> indexs) {
		this.url = url;
		this.indexs = indexs;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public HashMap<String, Integer> getIndexs() {
		return indexs;
	}
	public void setIndexs(HashMap<String, Integer> indexs) {
		this.indexs = indexs;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
}
