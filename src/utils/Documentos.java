package utils;


import java.net.URISyntaxException;
import java.net.UnknownHostException;
import com.mongodb.*;

public class Documentos {


	private Mongo mongo;
	private  DB db;
	private DBCollection collection;

	private String DBName = "RI";
	private String CollectionName = "documentosColetados";
	private String URL = "url";
	private String TAMANHO = "tamanho";
	private String DOCUMENTO = "documento";
	/*
	 * Cria a colle��o caso ainda n�o exista
	 */
	@SuppressWarnings("deprecation")
	public Documentos() {
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
	 * Insere se o documento ainda nao existir
	 */
	public void insert(String url, String documento, Integer tamanho) {
		try{
			BasicDBObject document = new BasicDBObject();
			document.put(URL, url);
			document.put(TAMANHO, tamanho);
			document.put(DOCUMENTO, documento);

			if(!find(url))
				collection.insert(document);

		}catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Procura por uma url no banco
	 */
	public String getDocumento(String url) {
		String documento = "";
		try{
			BasicDBObject resultado = new BasicDBObject();
			BasicDBObject pesquisa = new BasicDBObject();

			pesquisa.put(URL, url);
			resultado =  (BasicDBObject) collection.findOne(pesquisa);
			documento = (String) resultado.get(DOCUMENTO);

		}catch (Exception e) {

		}
		return documento;
	}
	
	public void setDocumento(String url, String documento) {

		try{

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append(DOCUMENTO, documento));

			BasicDBObject searchQuery = new BasicDBObject().append(URL, url);

			collection.update(searchQuery, newDocument);

		}catch (Exception e) {

		}
	}
	
	public Integer getTamanho(String url) {
		Integer tamanho = 0;
		try{
			BasicDBObject resultado = new BasicDBObject();
			BasicDBObject pesquisa = new BasicDBObject();

			pesquisa.put(URL, url);
			resultado =  (BasicDBObject) collection.findOne(pesquisa);
			tamanho = (Integer) resultado.get(TAMANHO);

		}catch (Exception e) {

		}
		return tamanho;
	}

	
	public void setTamanho(String url, int tamanho) {

		try{

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append(TAMANHO, tamanho));

			BasicDBObject searchQuery = new BasicDBObject().append(URL, url);

			collection.update(searchQuery, newDocument);

		}catch (Exception e) {

		}
	}
	
	public boolean find(String url) {
		try{
			BasicDBObject document = new BasicDBObject();
			document.put(URL, url);

			if( collection.findOne(document) != null)
				return true;


		}catch (MongoException e) {
			e.printStackTrace();
		}
		return false;

	}

	/*
	 * Print na tela com todos os elementos
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
	 * Deleta uma url no banco
	 */
	public void delete(String url) throws URISyntaxException {
		BasicDBObject document = new BasicDBObject();
		document.put(URL, url);

		collection.remove(document);
	}

	/*
	 * Deleta toda a colecao no banco
	 */
	public void deleteCollection() {
		collection.drop();
	}

	public static void main(String[] args) throws UnknownHostException, URISyntaxException{

		Documentos url = new Documentos();

		//url.deleteCollection();
		url.insert("www.google.com", "documento xxx", 2);
		url.insert("teste.com", "documento yy", 3);
		url.printCollection();
		System.out.println(url.find("www.google.com"));
		System.out.println(url.getTamanho("www.google.com"));
		System.out.println(url.getDocumento("www.google.com"));
		
		url.setTamanho("www.google.com", 10);
		url.setDocumento("www.google.com", "documento zzz");
		
		url.printCollection();
		
		//url.printCollection();

	}

}
