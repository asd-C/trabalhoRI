package utils;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import com.mongodb.*;

public class URLsVisitadas {


	private Mongo mongo;
	private  DB db;
	private DBCollection collection;

	private String DBName = "urlsVisitadasDatabase";
	private String CollectionName = "urlsVisitadas";
	private String URL = "url";

	/*
	 * Cria a colle��o caso ainda n�o exista
	 */
	@SuppressWarnings("deprecation")
	public URLsVisitadas() {
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
	public void insert(String url) {
		try{
			BasicDBObject document = new BasicDBObject();
			document.put(URL, url);

			if(!find(url))
				collection.insert(document);

		}catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Procura por uma irl no banco
	 */
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

		URLsVisitadas url = new URLsVisitadas();

		url.deleteCollection();
		url.insert("google");
		url.insert("teste");
		url.printCollection();
		System.out.println(url.find("googleee"));
		url.printCollection();

	}

}
