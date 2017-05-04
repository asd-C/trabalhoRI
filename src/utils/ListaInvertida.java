package utils;


import java.net.UnknownHostException;
import java.util.ArrayList;
import com.mongodb.*;

public class ListaInvertida {


	private Mongo mongo;
	private  DB db;
	private DBCollection collection;
	
	private String DBName = "listaInvertidaDatabase";
	private String CollectionName = "listaInvertida";
	private String TOKEN = "token";
	private String DOCUMENTOS = "documentos";

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
	public void insert(String token, String documento) {
		try{

			BasicDBObject pesquisaToken = new BasicDBObject();
			BasicDBObject resultadoToken = new BasicDBObject();
			ArrayList<String> documentos = new ArrayList<String>();

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			if(resultadoToken == null){
				//cria o token e add documento
				documentos.add(documento);
				pesquisaToken.put(DOCUMENTOS, documentos);
				collection.insert(pesquisaToken);

			}else{
				
				BasicDBList listaDocumentos =   (BasicDBList) resultadoToken.get(DOCUMENTOS);
				boolean existe = false;
				//confere se o token ja possui o documento
				for(int i = 0; i < listaDocumentos.size(); i++){
					if(listaDocumentos.get(i).equals(documento)){
						existe = true;
						break;
					}
				}
				//insere documento na colecao
				if(!existe){
					listaDocumentos.add(documento);
					resultadoToken.put(DOCUMENTOS, listaDocumentos);
					collection.findAndModify(pesquisaToken, resultadoToken);
				}
			}

		}catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Encontra todos os documentos do token
	 */
	public BasicDBList getDocumentos(String token) {

		BasicDBObject pesquisaToken = new BasicDBObject();
		BasicDBObject resultadoToken = new BasicDBObject();
		BasicDBList listaDocumentos = null;
		try{

			pesquisaToken.put(TOKEN, token);
			resultadoToken =  (BasicDBObject) collection.findOne(pesquisaToken);

			if(resultadoToken != null){
				listaDocumentos =   (BasicDBList) resultadoToken.get(DOCUMENTOS);
			}

		}catch (MongoException e) {
			e.printStackTrace();
		}

		return listaDocumentos;
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
		listaInvertida.insert("RI", "www.google.com");
		listaInvertida.insert("RI", "www.facebook.com");
		listaInvertida.printCollection();
		System.out.println(listaInvertida.getDocumentos("RI"));

	}

}
