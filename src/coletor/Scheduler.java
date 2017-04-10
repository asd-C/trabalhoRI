package coletor;

import java.util.ArrayList;
import java.util.Set;

import entity.Seeds;

public class Scheduler {

	/**
	 * Adiciona a base de urls, novas urls nao repetidas.
	 * 
	 * @param urls	Lista de urls nao repetidas.
	 * */
	public void addNewUrls(Set<String> urls) {
		Seeds.addUrls(urls);
	}
	

	/**
	 * Gera uma nova lista de urls para coletar.
	 * 
	 * @return	lista de novas urls para coletar.
	 * */
	public ArrayList<String> generateNewSeedsList() {
		return Seeds.generateNewSeeds().getUrls();
	}
	
	public DomainUrls generateNewSeeds() {
		return Seeds.generateNewSeeds();
	}
}
