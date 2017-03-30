package coletor;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;

public class DNSResolver {


		private Mongo mongo;
		private  DB db;
		private DBCollection collection;
		
		public DNSResolver() {
			try{
				mongo = new Mongo("localhost", 27017);
				db = mongo.getDB("dnsDatabase");
				collection = db.getCollection("dns");
				
			}catch (UnknownHostException e) {
				e.printStackTrace();
		    } catch (MongoException e) {
		    	e.printStackTrace();
		    }
			
		}
		
		public void insert(String url, String ip) {
			try{
				BasicDBObject document = new BasicDBObject();
				document.put("url", url);
				document.put("ip", ip);
				
				collection.insert(document);
				
			}catch (MongoException e) {
		    	e.printStackTrace();
		    }
			
		}
		
		public String findIp(String url) {
			try{
				
				DBCollection table = db.getCollection("dns"); //my Collection
				BasicDBObject courseNameDBObject = new BasicDBObject("url", url) ;
				DBCursor cursor = table.find(courseNameDBObject);
				
				
				
				while(cursor.hasNext()){
					return (String) cursor.next().get("ip");
				}
				
				
			}catch (MongoException e) {
		    	e.printStackTrace();
		    }
			return null;
			
		}
		
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
		
		public void deleteCollection() {
			collection.drop();
		}
		
		public String getDomainName(String url) throws URISyntaxException {
		    URI uri = new URI(url);
		    String domain = uri.getHost();
		    return domain.startsWith("www.") ? domain.substring(4) : domain;
		}

		public ArrayList<String> addListURL(ArrayList<String> urlsArray) throws URISyntaxException, MalformedURLException, UnknownHostException {
			URL url;
			ArrayList<String> new_ips = new ArrayList<String>();
			// TODO Auto-generated method stub
			for (String urlArray: urlsArray) {
				
				InetAddress address = InetAddress.getByName(new URL(urlArray).getHost());
				String dominio = getDomainName(urlArray);
				
				String ip = address.getHostAddress();
				
				System.out.println(dominio + " " + ip);
				
				insert(dominio, ip.toString());
				new_ips.add(ip.toString());
			}
			
			return new_ips;
		}
		
//		public static void main(String[] args) throws UnknownHostException{
//			
//			DNSResolver dns = new DNSResolver();
//			
//			dns.deleteCollection();
//			dns.insert("google", "145.2.1.1");
//			dns.insert("teste", "145.3.2.1");
//			dns.printCollection();
//			
//			System.out.print(dns.findIp("google"));
//			
//			
//		}

}
