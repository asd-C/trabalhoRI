package utils;

import java.util.HashMap;

import global.Global;
import utils.dataManager.TextCompressor;

public class Writer {
	
	private long increment;
	
	public boolean saveWithCompression(HashMap<String, String> contents) {
		
		increment = 0;
		
		contents.forEach((url, text) -> {
			
			String filepath = Global.pathFormat(Global.dir_root, Global.dir_document, Global.digestSHA1(url));
			TextCompressor.zipFromString(filepath, text);
			increment += text.length();
			
		});
		
		Global.addSize_all_file_without_compression(increment);
		
		return true;
	}
}
