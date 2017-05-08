package entity.coletor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.codehaus.jackson.type.TypeReference;

import global.Global;
import global.Manager;

public class SeedsManager extends Manager{
	
	public SeedsManager() {
		loadSeeds();
	}
	
	private static String path_unvisited = Global.pathFormat(
			Global.dir_root, 
			Global.dir_seed, 
			Global.file_unvisited_seed);
	
	private static String path_visited = Global.pathFormat(
			Global.dir_root, 
			Global.dir_seed, 
			Global.file_visited_seed);
	
	public synchronized void saveSeeds() {
		
		try {
			Global.log("Saving unvisited urls to " + path_unvisited + "...");
			Global.log("Saving visited urls to " + path_visited + "...");
			save(Global.objectMapper.writeValueAsString(Seeds.getDomains_unvisited_urls()), path_unvisited);
			save(Global.objectMapper.writeValueAsString(Seeds.getDomains_visited_urls()), path_visited);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void loadSeeds() {

		Global.log("Loading unvisited urls from " + path_unvisited + "...");
		Global.log("Loading visited urls from " + path_visited + "...");

		String result1 = load(path_unvisited);
		String result2 = load(path_visited);
		if (result1 == null) {
			
			Global.log("No unvisited urls file is found.");
			String[] urls = new String[] {"https://en.wikipedia.org/wiki/Category:Living_people", 
					"https://en.wikipedia.org/wiki/barack_obama"};
			Seeds.addUrls(new HashSet<String>(Arrays.asList(urls)));
		} else if (result2 == null) {
			
			Global.log("No visited urls file is found.");
		} else {
			
			try {
			    TypeReference<HashMap<String, DomainSeeds>> typeRef = new TypeReference<HashMap<String, DomainSeeds>>() {};

	            Seeds.setDomains_unvisited_urls(Global.objectMapper.readValue(result1, typeRef));
	            Seeds.setDomains_visited_urls(Global.objectMapper.readValue(result2, typeRef));
	            
				Global.log("Unvisited urls file is found, with " + Seeds.getUnvisitedSeeds() + " items.");
				Global.log("Visited urls file is found, with " + Seeds.getVisitedSeeds() + " items.");
			} catch (IOException e) {
				e.printStackTrace();
				Global.log("Loading urls files failed.");
			}
		}
	}
}
