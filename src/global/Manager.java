package global;

import utils.dataManager.TextCompressor;

public class Manager {
	
	public synchronized void save(String content, String filepath) {
		TextCompressor.zipFromString(filepath, content);
	}
	
	public synchronized String load(String filepath) {
		return TextCompressor.unzipToString(filepath);
	}
}
