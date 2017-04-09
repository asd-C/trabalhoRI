package coletor;

import java.util.ArrayList;

public class DomainUrls {
	private String domain;
	private ArrayList<String> urls;
	
	public DomainUrls() {
		setUrls(new ArrayList<String>());
	}
	
	public DomainUrls(String domain, ArrayList<String> urls) {
		this.domain = domain;
		this.urls = urls;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public ArrayList<String> getUrls() {
		return urls;
	}
	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}
}
