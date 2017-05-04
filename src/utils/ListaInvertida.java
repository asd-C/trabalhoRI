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

public class ListaInvertida {


	private Mongo mongo;
	private  DB db;
	private DBCollection collection;

	private String DBName = "RI";
	private String CollectionName = "listaInvertida";
	private String TOKEN = "token";
	private String NI = "ni";
	private String LISTA = "lista";
	private String DOCUMENTOS = "documentos";
	private String FI = "fi";

	/*
	 * Cria o banco e a colecao caso ainda nao exista
	 */
	@SuppressWarnings("deprecation")
	public ListaInvertida() {
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
	public void insert(String token, String documento, String fi) {
		try{
			BasicDBObject teste = new BasicDBObject();

			BasicDBObject pesquisaToken = new BasicDBObject();
			BasicDBObject objDocumentos = new BasicDBObject();
			BasicDBObject resultadoToken = new BasicDBObject();
			ArrayList<BasicDBObject> documentos = new ArrayList<BasicDBObject>();

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			if(resultadoToken == null){
				//cria o token e add documento
				pesquisaToken.put(NI, "0");
				objDocumentos.put(DOCUMENTOS, documento);
				objDocumentos.put(FI, fi);

				documentos.add(objDocumentos);

				pesquisaToken.put(LISTA, documentos);
				collection.insert(pesquisaToken);

			}else{

				BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTA);
				BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);

				//procura se o documento ja existe
				boolean existe = false;
				for(BasicDBObject dbObj : valuesArray) {
					if(dbObj.getString(DOCUMENTOS).equals(documento)){
						existe = true;
						break;
					}
				}

				//insere documento na colecao
				if(!existe){
					objDocumentos.put(DOCUMENTOS, documento);
					objDocumentos.put(FI, fi);

					listaDocumentos.put(listaDocumentos.size(), objDocumentos);

					BasicDBObject newDocument = new BasicDBObject();
					newDocument.append("$set", new BasicDBObject().append(LISTA, listaDocumentos));

					BasicDBObject searchQuery = new BasicDBObject().append(TOKEN, token);

					collection.update(searchQuery, newDocument);
				}
			}

		}catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Retorna a quantidade de documentos que foi encontrado o token
	 */
	public Integer getNi(String token) {
		String ni = "0";
		try{
			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);
			ni = (String) resultadoToken.get(NI);


			System.out.println("kkk " + ni);

		}catch (Exception e) {

		}
		return Integer.parseInt(ni);

	}
	
	/*
	 * Set na quantidade de documentos do token
	 */
	public void setNi(String token, int ni) {

		try{

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append(NI, ni));

			BasicDBObject searchQuery = new BasicDBObject().append(TOKEN, token);

			collection.update(searchQuery, newDocument);

		}catch (Exception e) {

		}
	}

	/*
	 * Retorna o fi de um documento que foi encontrado em um token
	 */
	public Integer getFi(String token, String documento) {
		String fi = "0";
		try{
			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTA);
			BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);
			for(BasicDBObject dbObj : valuesArray) {
				if(dbObj.getString(DOCUMENTOS).equals(documento)){
					fi = dbObj.getString(FI);
					break;
				}
			}



		}catch (Exception e) {

		}

		return Integer.parseInt(fi);
	}

	/*
	 * Set no fi de um documento que foi encontrado em um token
	 */
	public void setFi(String token, String documento, String fi) {

		try{
			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTA);
			BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);

			for(BasicDBObject dbObj : valuesArray) {
				if(dbObj.getString(DOCUMENTOS).equals(documento)){
					dbObj.append(FI, fi);
					break;
				}
			}
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append(LISTA, valuesArray));

			BasicDBObject searchQuery = new BasicDBObject().append(TOKEN, token);

			collection.update(searchQuery, newDocument);



		}catch (Exception e) {

		}
	}


	/*
	 * Retorna toda a lista de documentos de um determinado token
	 */
	public Map<String, Integer> getListaCompletaToken(String token) {
		Map<String, Integer> listaCompleta = new HashMap<String, Integer>();
		try{

			BasicDBObject resultadoToken = new BasicDBObject();
			BasicDBObject pesquisaToken = new BasicDBObject();

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			BasicDBList listaDocumentos = (BasicDBList) resultadoToken.get(LISTA);
			BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);

			for(BasicDBObject dbObj : valuesArray) {
				String documento = dbObj.getString(DOCUMENTOS);
				String fi = dbObj.getString(FI);
				listaCompleta.put(documento, Integer.parseInt(fi));

			}



		}catch (Exception e) {

		}

		return listaCompleta;
	}

	/*
	 * Retorna lista com toda a colecao
	 */
	public Map<String, Map<String, Integer>> getListaCompleta() {
		Map<String, Map<String, Integer>> listaCompleta = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> listaElementos;
		try{

			DBCursor cursor = collection.find();
			while (cursor.hasNext()) {
				listaElementos = new HashMap<String, Integer>();
				DBObject obj = cursor.next();

				String token = (String) obj.get(TOKEN);
				BasicDBList listaDocumentos = (BasicDBList) obj.get(LISTA);

				BasicDBObject[] valuesArray = listaDocumentos.toArray(new BasicDBObject[0]);


				for(BasicDBObject dbObj : valuesArray) {
					String documento = dbObj.getString(DOCUMENTOS);
					String fi = dbObj.getString(FI);
					listaElementos.put(documento, Integer.parseInt(fi));

				}

				listaCompleta.put(token, listaElementos);

			}

		}catch (Exception e) {

		}

		return listaCompleta;
	}
	
	/*
	 * Print de toda collecao
	 */
	public void printCollection() {
		try{

			DBCursor cursor = collection.find();
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				System.out.println(obj);
			}


		}catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Apaga toda colecao do banco
	 */
	public void deleteCollection() {
		collection.drop();
	}


	public static void main(String[] args) throws UnknownHostException{

		ListaInvertida listaInvertida = new ListaInvertida();

		//listaInvertida.deleteCollection();
		listaInvertida.insert("RI", "www.google.com", "2");

		System.out.println("RI");
		
		/*teste1*/
		//				Map<String, Integer> c = listaInvertida.getListaCompletaToken("RI");
		//				
		//				Set<Entry<String,Integer>> set = c.entrySet();
		//				Iterator it =  set.iterator();
		//				while(it.hasNext()){
		//					Entry<String, Integer> entry = (Entry)it.next();
		//					System.out.println(entry.getKey() + "\t\t"+entry.getValue());
		//				}

		/*teste2*/

//				Map<String, Map<String, Integer>> c = listaInvertida.getListaCompleta();
//				
//				Set<Entry<String,Map<String, Integer>>> set = c.entrySet();
//				Iterator it =  set.iterator();
//				while(it.hasNext()){
//					Entry<String, Integer> entry = (Entry)it.next();
//					System.out.println(entry.getKey() + "\t\t"+entry.getValue());
//				}
		//listaInvertida.setNi("RI", 89);


	}

}
