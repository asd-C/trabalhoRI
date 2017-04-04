package coletor;

import java.io.*;
import java.util.ArrayList;
import java.net.*;

public class FilterUrls {
	private static final CharSequence DISALLOW = null;

	private static ArrayList<String> getDisallow(String domain) {
		String subLine[];
		ArrayList<String> disallow = new ArrayList<String>();
		char letraInicial;
		char letraFinal;

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(new URL(domain + "/robots.txt").openStream()))) {
			String line = null;
			while ((line = in.readLine()) != null) {
				// System.out.println(line);
				subLine = line.split(" ");
				if (subLine[0].equals("Disallow:")) {
					String parte = subLine[1];
					letraInicial = parte.charAt(0);
					letraFinal = parte.charAt(0);
					int inicio = 0, fim = parte.length();

					if (letraInicial == '*') {
						inicio++;
					}

					if (letraFinal == '*') {
						fim--;
					}

					// System.out.println(subLine[1]);
					disallow.add(subLine[1].substring(inicio, fim));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return disallow;
	}

	private static boolean isSorted(Integer[] posicoes) {
		for (int i = 0; i < posicoes.length - 1; i++) {
			if (posicoes[i] > posicoes[i + 1]) {
				return false;
			}
		}
		return true;

	}

	private static boolean contemPalavra(ArrayList<String> dominiosProibidos, String url) {
		Integer[] posicoes = null;
		int posicao;
		int contem;
		for (int i = 0; i < dominiosProibidos.size(); i++) {
			if (url.toLowerCase().contains(dominiosProibidos.get(i).toLowerCase())
					|| (url + "/").toLowerCase().contains(dominiosProibidos.get(i).toLowerCase())) {
				return true;
			} else if (dominiosProibidos.get(i).contains("*")) {
				contem = 0;
				String[] palavras = dominiosProibidos.get(i).split("\\*");
				posicoes = new Integer[palavras.length];
				for (int j = 0; j < palavras.length; j++) {
					posicao = url.toLowerCase().indexOf(palavras[j]);
					if (posicao > 0) {
						posicoes[j] = posicao;
						contem++;
					}
				}
				if (contem > 1 && isSorted(posicoes))
					return true;

			}

		}
		return false;
	}

	public static ArrayList<String> filtrar(DomainUrls domain) {
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

	public static void main(String[] args) throws UnsupportedEncodingException {
		ArrayList<String> urls = new ArrayList<String>();
		urls.add("http:/google.com/manage/reserve/b/");
		urls.add("http:/google.com");
		urls.add("http:/google.com/compare/qualquercoisa/apply");
		urls.add("http:/google.com/compare/apply");
		urls.add("http:/google.com/compare/apply/qualquercoisa");

		DomainUrls domain = new DomainUrls("https://google.com", urls);

		// ---------------------------

		ArrayList<String> urlsPermitidas = filtrar(domain);

		for (int i = 0; i < urlsPermitidas.size(); i++) {
			System.out.println(urlsPermitidas.get(i));
		}

	}
}
