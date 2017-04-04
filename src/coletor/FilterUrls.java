package coletor;

import java.net.URL;
import java.io.*;
import java.util.ArrayList;

public class FilterUrls {

	public ArrayList<String> getDisallow(String domain) {
		String subLine[];
		ArrayList<String> disallow = new ArrayList<String>();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(domain).openStream()))) {
			String line = null;
			while ((line = in.readLine()) != null) {
				subLine = line.split(" ");
				if (subLine[0].equals("Disallow:")) {
					String parte = subLine[1];
					char letraInicial = parte.charAt(0);
					char letraFinal = parte.charAt(0);
					int inicio = 0, fim = parte.length();

					if (letraInicial == '*') {
						inicio++;
					}

					if (letraFinal == '*') {
						fim--;
					}
					// * no meio
					/// ?hl=*&
					// /?hl=*&*&gws_rd=ssl
					System.out.println(subLine[1]);
					disallow.add(subLine[1].substring(inicio, fim));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return disallow;

	}

	public boolean contemPalavra(ArrayList<String> dominiosProibidos, String url) {
		for (int i = 0; i < dominiosProibidos.size(); i++) {
			if (url.toLowerCase().contains(dominiosProibidos.get(i).toLowerCase())
					|| (url + "/").toLowerCase().contains(dominiosProibidos.get(i).toLowerCase()))
				return true;
		}
		return false;
	}

	public ArrayList<String> filtrar(DomainUrls domain) {
		ArrayList<String> dominiosProibidos = new ArrayList<String>();
		ArrayList<String> urlsPermitidas = new ArrayList<String>();

		dominiosProibidos = getDisallow(domain.getDomain());
		ArrayList<String> urls = domain.getUrls();

		for (int i = 0; i < urls.size(); i++) {
			if (!contemPalavra(dominiosProibidos, urls.get(i))) {
				urlsPermitidas.add(urls.get(i));
			}

		}
		return urlsPermitidas;
	}

}
