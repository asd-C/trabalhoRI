package coletor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Fetcher {
	
	private final String AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0";
	private final String ACCEPT_LANG = "Accept-Language";
	private final String LANG = "en";
	
	public static int delay = 100;
	
	/**
	 * Dada uma url, recupera-se o conteudo da pagina.
	 * Caso o conteudo da pagina for multimidia, sera lancada uma excecao.
	 * 
	 * @param url	A url no qual se encontra a pagina Web, pode ser dominio ou ip.
	 * @return		O conteudo da pagina numa instancia de Document.
	 * */
	public Document fetch(String url) {
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url)
					.userAgent(AGENT)
					.header(ACCEPT_LANG, LANG)
					.get();
	
			Thread.sleep(delay);
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
		
		return doc;
	}
	
	/**
	 * Dada uma lista de urls, recupera-se o conteudo de todas as paginas.
	 * 
	 * @param urls	O conjunto de urls que vao ser processados para recuperar seus conteudos.
	 * @return		Um hashmap onde chave e url, e valor e conteudo da url.
	 * */
	public HashMap<String, Document> fetchAll(ArrayList<String> urls) {
		HashMap<String, Document> pages = new HashMap<String, Document>();
		Document doc;
		
		for (String url: urls) {
			doc = fetch(url);
			
			if (doc != null) pages.put(url, doc);
		}
		
		return pages;
	}
}
