package utils;

import global.Global;
import utils.dataManager.TextCompressor;

public class Reader {
	
	public static int length = 500;
	
	public static String getFileByUrl(String url) {
		 
		String filepath = Global.pathFormat(Global.dir_root, Global.dir_document, Global.digestSHA1(url));
		 
		return TextCompressor.unzipToString(filepath).substring(0, length);
	
	}
}
