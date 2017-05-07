package entity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import coletor.DomainUrls;


public class Seeds {
	public static int seeds_size = 30; 
	
	private static int total;
	public static synchronized int getVisitedSeeds() {
		total = 0;
		domains_visited_urls.forEach((key, value) -> total +=value.getUrls().size());
		return total; 
	}
	
	public static synchronized int getUnvisitedSeeds() { 
		total = 0;
		domains_unvisited_urls.forEach((key, value) -> total +=value.getUrls().size());
		return total; 
	}
	
	public static synchronized void showVisitedSeeds() {
		System.out.println("---------------- Visited urls ----------------");
		for (Entry<String, DomainSeeds> en: domains_visited_urls.entrySet()) {
			Set<String> urls = en.getValue().getUrls();
			System.out.println(en.getKey());
			for (String url: urls) {
				System.out.print(url + ", ");
			}
			System.out.println();
		}
	}
	
	public static synchronized void showUnvisitedSeeds() {
		System.out.println("---------------- Unvisited urls ----------------");
		for (Entry<String, DomainSeeds> en: domains_unvisited_urls.entrySet()) {
			Set<String> urls = en.getValue().getUrls();
			System.out.println(en.getKey());
			for (String url: urls) {
				System.out.print(url + ", ");
			}
			System.out.println();
		}
	}
	
	private static HashMap<String, DomainSeeds> domains_visited_urls = 
			new HashMap<String, DomainSeeds>();
	private static HashMap<String, DomainSeeds> domains_unvisited_urls = 
			new HashMap<String, DomainSeeds>();
	
	public static HashMap<String, DomainSeeds> getDomains_visited_urls() {
		return domains_visited_urls;
	}

	public static void setDomains_visited_urls(HashMap<String, DomainSeeds> domains_visited_urls) {
		Seeds.domains_visited_urls = domains_visited_urls;
	}

	public static HashMap<String, DomainSeeds> getDomains_unvisited_urls() {
		return domains_unvisited_urls;
	}

	public static void setDomains_unvisited_urls(HashMap<String, DomainSeeds> domains_unvisited_urls) {
		Seeds.domains_unvisited_urls = domains_unvisited_urls;
	}

	private static DomainSeeds createDomainSeeds(String url, String domain) {
		String protocol = "";
	    int i = 0;
	    while (url.charAt(i) != ':') { // get protocol used by the web server
	    	protocol += url.charAt(i);
	    	i++;
	    }
	    
	    return new DomainSeeds(domain, protocol + "://");
	}
	
	private static String getDomain(String url) throws URISyntaxException {
		URI uri = new URI(url);
	    String domain = uri.getHost();
	    String tmp = "";

	    try {
	    	tmp = domain.startsWith("www.") ? domain.substring(4) : domain;
	    } catch (Exception e) { 
	    	System.out.println("url: " + url);
	    	System.out.println("domain: " + domain);
	    	
	    	e.printStackTrace(); 
	    }
	    return tmp;
	}
	
	public static synchronized void addUrls(Set<String> seeds) {
		String domain;
		DomainSeeds urls;
		for (String seed: seeds) {
			try {
				domain = getDomain(seed);
				urls = domains_unvisited_urls.get(domain);
				
				// if there is no mapped entity, 
				// create one and mapped it with its domain.
				if (urls == null) {
					urls = createDomainSeeds(seed, domain);
					domains_unvisited_urls.put(domain, urls);
				}
				
				// do not need check out if seed is duplicate, 
				// because it uses Set.
				urls.getUrls().add(seed);
			} catch (URISyntaxException e) {}
		}
	}
	
	public static synchronized DomainUrls generateNewSeeds(String domain) {
		ArrayList<String> seeds = new ArrayList<String>();
		String[] urls_array;
		DomainSeeds urls = null, urls2 = null;
		
		urls = domains_unvisited_urls.get(domain); 
		
		// if there is no set with urls, return arraylist with zero element.
		if (urls == null || urls.getUrls().size() == 0) return new DomainUrls();
		
		// collecting urls
		urls_array = new String[urls.getUrls().size()];
		urls_array = urls.getUrls().toArray(urls_array);
		for (int i=0; i<seeds_size; i++) {
			if (i < urls_array.length) seeds.add(urls_array[i]); 
		}
		
		urls2 = domains_visited_urls.get(urls.getDomain()); 
		if (urls2 == null) {
			urls2 = new DomainSeeds(urls.getDomain(), urls.getProtocol());
			domains_visited_urls.put(urls.getDomain(), urls2);
		}
		
		for (String tmp: seeds) {
			urls.getUrls().remove(tmp);
			urls2.getUrls().add(tmp);
		}
		
		return new DomainUrls(urls.getProtocol()+urls.getDomain(), seeds);
	}
	
	public static synchronized DomainUrls generateNewSeeds() {
		ArrayList<String> seeds = new ArrayList<String>();
		String[] urls_array;
		DomainSeeds urls = null, urls2 = null;
		
		// find a set which contains some urls (seeds).
		int i = 0;
		while (i < domains_unvisited_urls.size()) {
			urls = domains_unvisited_urls.get(domains_unvisited_urls.keySet().toArray()[i]); 
			
			if (urls.getUrls().isEmpty() == false) {
				break;
			}
			i++;
		}
		
		// if there is no set with urls, return arraylist with zero element.
		if (urls == null) return new DomainUrls();
		
		// collecting urls
		urls_array = new String[urls.getUrls().size()];
		urls_array = urls.getUrls().toArray(urls_array);
		for (i=0; i<seeds_size; i++) {
			if (i < urls_array.length) seeds.add(urls_array[i]); 
		}
		
		urls2 = domains_visited_urls.get(urls.getDomain()); 
		if (urls2 == null) {
			urls2 = new DomainSeeds(urls.getDomain(), urls.getProtocol());
			domains_visited_urls.put(urls.getDomain(), urls2);
		}
		
		for (String tmp: seeds) {
			urls.getUrls().remove(tmp);
			urls2.getUrls().add(tmp);
		}
		
		return new DomainUrls(urls.getProtocol()+urls.getDomain(), seeds);
	}
	
	public static void main(String... args) {
		try {
			Set<String> urls = new HashSet<String>();
			urls.add("http://www.google.com");
			urls.add("http://www.google.com/get");
			urls.add("http://www.google.com/search");
			urls.add("http://www.globo.com");
			urls.add("http://www.globo.com/hello");
			
			addUrls(urls);
			
			System.out.println(getUnvisitedSeeds());
			ArrayList<String> a = generateNewSeeds().getUrls();
			for (String tmp: a) {
				System.out.println(tmp);
			}
			System.out.println(getUnvisitedSeeds());
			System.out.println(getVisitedSeeds());
			
			
			String url = "http://www.google.com";
			DomainSeeds ds = createDomainSeeds(url, getDomain(url));
			System.out.println(ds.getProtocol() + ds.getDomain());
			System.out.println(ds.getUrls().size());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class DomainSeeds {
	private String domain;
	private Set<String> urls;
	private String protocol;
	
	public DomainSeeds() {}
	
	public DomainSeeds(String domain, String protocol) {
		this.domain = domain;
		this.protocol = protocol;
		setUrls(new HashSet<String>());
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Set<String> getUrls() {
		return urls;
	}
	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
}