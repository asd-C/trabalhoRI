package utils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mongodb.*;

public class URLs {


	private Mongo mongo;
	private  DB db;
	private DBCollection collection;

	private String DBName = "RI";
	private String CollectionName = "urlsEncontradas";
	
	private String DOMINIO = "dominio";
	private String LISTAURL = "urls";
	private String VISITADO = "visitado";
	private String URL = "url";

	/*
	 * Cria o banco e a colecao caso ainda nao exista
	 */
	@SuppressWarnings("deprecation")
	public URLs() {
		try{
			mongo = new Mongo("localhost", 27017);
			db = mongo.getDB(DBName);
			collection = db.getCollection(CollectionName);

		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Insere na colecao ListaInvertida caso o token ou o documento ainda nao exista
	 */
	public void insert(String dominino, String url, boolean visitado) {
		try{
			BasicDBObject teste = new BasicDBObject();

			BasicDBObject pesquisaToken = new BasicDBObject();
			BasicDBObject objDocumentos = new BasicDBObject();
			BasicDBObject resultadoToken = new BasicDBObject();
			ArrayList<BasicDBObject> documentos = new ArrayList<BasicDBObject>();

			pesquisaToken.put(DOMINIO, dominino);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			if(resultadoToken == null){
				//cria o token e add documento
				objDocumentos.put(URL, url);
				objDocumentos.put(VISITADO, visitado);

				documentos.add(objDocumentos);

				pesquisaToken.put(LISTAURL, documentos);
				collection.insert(pesquisaToken);

			}else{

				BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTAURL);
				BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);

				//procura se o documento ja existe
				boolean existe = false;
				for(BasicDBObject dbObj : valuesArray) {
					if(dbObj.getString(URL).equals(url)){
						existe = true;
						break;
					}
				}

				//insere documento na colecao
				if(!existe){
					objDocumentos.put(URL, url);
					objDocumentos.put(VISITADO, visitado);

					listaDocumentos.put(listaDocumentos.size(), objDocumentos);

					BasicDBObject newDocument = new BasicDBObject();
					newDocument.append("$set", new BasicDBObject().append(LISTAURL, listaDocumentos));

					BasicDBObject searchQuery = new BasicDBObject().append(DOMINIO, dominino);

					collection.update(searchQuery, newDocument);
				}
			}

		}catch (MongoException e) {
			e.printStackTrace();
		}

	}



	/*
	 * Retorna o visitado de um documento que foi encontrado em um token
	 */
	public String getVisitado(String token, String url) {
		String visitado = "";
		try{
			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(DOMINIO, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTAURL);
			BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);
			for(BasicDBObject dbObj : valuesArray) {
				if(dbObj.getString(URL).equals(url)){
					visitado = dbObj.getString(VISITADO);
					break;
				}
			}



		}catch (Exception e) {

		}

		return visitado;
	}
//
//	/*
//	 * Set no fi de um documento que foi encontrado em um token
//	 */
	public void setVisitado(String dominio, String url, String visitado) {

		try{
			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(DOMINIO, dominio);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTAURL);
			BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);

			for(BasicDBObject dbObj : valuesArray) {
				if(dbObj.getString(URL).equals(url)){
					dbObj.append(VISITADO, visitado);
					break;
				}
			}
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append(LISTAURL, valuesArray));

			BasicDBObject searchQuery = new BasicDBObject().append(DOMINIO, dominio);

			collection.update(searchQuery, newDocument);



		}catch (Exception e) {

		}
	}


//	/*
//	 * Retorna toda a lista de documentos de um determinado token
//	 */
	public Map<String, String> getListaCompletaDominio(String dominio) {
		Map<String, String> listaCompleta = new HashMap<String, String>();
		try{

			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(DOMINIO, dominio);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTAURL);
			BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);
			
			

			for(BasicDBObject dbObj : valuesArray) {
				String url = dbObj.getString(URL);
				String visitado = dbObj.getString(VISITADO);
				listaCompleta.put(url, visitado);
			}

		}catch (Exception e) {

		}

		return listaCompleta;
	}
//
//	/*
//	 * Retorna lista com toda a colecao
//	 */
	public Map<String, Map<String, String>> getListaCompleta() {
		Map<String, Map<String, String>> listaCompleta = new HashMap<String, Map<String, String>>();
		Map<String, String> listaElementos;
		try{

			DBCursor cursor = collection.find();
			while (cursor.hasNext()) {
				listaElementos = new HashMap<String, String>();
				DBObject obj = cursor.next();

				String dominio =  (String) obj.get(DOMINIO);
				BasicDBList listaDocumentos = (BasicDBList) obj.get(LISTAURL);

				BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);


				for(BasicDBObject dbObj : valuesArray) {
					String url = dbObj.getString(URL);
					String visitado = dbObj.getString(VISITADO);
					listaElementos.put(url, visitado);

				}

				listaCompleta.put(dominio, listaElementos);

			}

		}catch (Exception e) {

		}

		return listaCompleta;
	}


	public static void main(String[] args) throws UnknownHostException{

		URLs listaInvertida = new URLs();

		//listaInvertida.deleteCollection();
		//listaInvertida.insert("RI", "www.google.com", "2");
		//System.out.println(listaInvertida.getVisitado("google", "www.google.com"));
		//System.out.println("RI");
		//listaInvertida.insert("google2", "www.google2.com", false);
		
		/*teste1*/
//						Map<String, String> c = listaInvertida.getListaCompletaDominio("google");
//						
//						Set<Entry<String,String>> set = c.entrySet();
//						Iterator it =  set.iterator();
//						while(it.hasNext()){
//							Entry<String, String> entry = (Entry)it.next();
//							System.out.println(entry.getKey() + "\t\t"+entry.getValue());
//						}

		/*teste2*/

		Map<String, Map<String, String>> c = listaInvertida.getListaCompleta();
				
				Set<Entry<String,Map<String, String>>> set = c.entrySet();
				Iterator it =  set.iterator();
				while(it.hasNext()){
					Entry<String, String> entry = (Entry)it.next();
					System.out.println(entry.getKey() + "\t\t"+entry.getValue());
				}
		//listaInvertida.setNi("RI", 89);


	}

}
