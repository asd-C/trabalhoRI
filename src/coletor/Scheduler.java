package coletor;

import java.util.ArrayList;
import java.util.Set;

import entity.Seeds;

public class Scheduler {
	
	public void addNewUrls(Set<String> urls) {
		Seeds.addUrls(urls);
	}
	
	public ArrayList<String> generateNewSeeds() {
		return Seeds.generateNewSeeds();
	}
}
