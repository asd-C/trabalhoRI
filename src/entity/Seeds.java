package entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Seeds {
	private static Set<String> visited_seeds = new HashSet<String>();
	private static Set<String> unvisited_seeds = new HashSet<String>();
	
	public static int seeds_size = 30; 
	
	public static synchronized int getVisitedSeeds() { return visited_seeds.size(); }
	public static synchronized int getUnvisitedSeeds() { return unvisited_seeds.size(); }
	
	public static synchronized void addUrls(Set<String> seeds) {
		for (String seed: seeds) {
			if (!visited_seeds.contains(seed)) {
				unvisited_seeds.add(seed);
			}
		}
	}
	
	public static synchronized ArrayList<String> generateNewSeeds() {
		ArrayList<String> seeds = new ArrayList<String>();
		String tmp;
		Iterator<String> it = unvisited_seeds.iterator();
		
		for (int i=0; i<seeds_size; i++) {
			if (it.hasNext()){
				tmp = it.next();
				seeds.add(tmp);
			} else {
				break;
			}
		}
		
		for (String seed: seeds) {
			unvisited_seeds.remove(seed);
			visited_seeds.add(seed);
		}
		
		return seeds;
	}
}