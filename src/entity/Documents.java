package entity;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Documents {
	private static Semaphore semaphore = new Semaphore(1); 
	private static HashMap<String, String> documents_text = new HashMap<String, String>();
	
	public static Semaphore getSemaphore() { return semaphore; }
	
	public static synchronized void addDocumentsText(HashMap<String, String> documents) {
		try {
			semaphore.acquire();
			Documents.documents_text.putAll(documents);
			semaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
