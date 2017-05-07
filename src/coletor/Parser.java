package coletor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	
	/**
	 * O metodo busca todas urls de uma pagina Web.
	 * As urls repetidas serao eliminadas, por ser um set.
	 *  
	 * @param doc	O documento que sera processado.
	 * @return 		Um conjunto de urls.
	 * */
	public Set<String> getURLsFromPage(Document doc) {
		Set<String> urls = new HashSet<String>();
		Elements links = doc.select("a[href]");
		String tmp;
		
		for (Element link: links) {
			tmp = link.attr("abs:href");
			if (tmp.isEmpty()) continue;
			try {
				urls.add(removeAnchor(tmp));
			} catch (Exception e) {}
		}
		
		return urls;
	}

	public String removeAnchor(String sUrl) throws URISyntaxException, MalformedURLException {
		URL url = new URL(sUrl);
		
		String protocol = url.getProtocol(); 
		if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) 
			throw new MalformedURLException();
		
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
		String canonical = uri.toString();
		return canonical;
	}
	
	/**
	 * O metodo busca todas urls de um conjunto de paginas Web.
	 * As urls repetidas serao eliminadas, por ser um set.
	 *  
	 * @param pages	Os documentos que serao processados.
	 * @return 		Um conjunto de urls.
	 * */
	public Set<String> getURLsFromPages(HashMap<String, Document> pages) {
		Set<String> urls = new HashSet<String>();
		
		pages.forEach((key, value) -> urls.addAll(getURLsFromPage(value)));
		
		return urls;
	}
	
	static String regex = "[^a-zA-Z0-9\\p{Punct}]";
	
	/**
	 * O metodo recupera o conteudo textual de uma pagina Web.
	 *  
	 * @param doc	O documento que sera processado.
	 * @return 		O conteudo textual da pagina.
	 * */
	public String getTextFromPage(Document doc) {
		return doc.select("body").text().replaceAll(regex, " ");
	}
	
	/**
	 * O metodo recupera o conteudo textual de um conjunto de paginas Web.
	 *  
	 * @param pages	Os documentos que serao processados.
	 * @return 		Um hashmap onde a chave e url e o valor e conteudo textual da pagina. 
	 * */
	public HashMap<String, String> getTextsFromPages(HashMap<String, Document> pages) {
		HashMap<String, String> contents = new HashMap<String, String>();
		
		pages.forEach((key, value) -> contents.put(key, getTextFromPage(value)));
		
		return contents;
	}
}
