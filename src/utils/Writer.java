package utils;

import java.util.HashMap;

import entity.coletor.Document;
import global.Global;
import utils.dataManager.TextCompressor;

public class Writer {
	
	public boolean saveWithCompression(HashMap<String, String> contents) {
		
		contents.forEach((url, text) -> {
			String filepath = Global.pathFormat(Global.dir_root, Global.dir_document, Global.digestSHA1(url));
			TextCompressor.zipFromString(filepath, text);
		});
		return true;
	}
}
