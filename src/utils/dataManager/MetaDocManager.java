package utils.dataManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.coletor.MetaDoc;

public class MetaDocManager {
	
	static {
		createDir();
	}
	public final static String dir_proc = "proc";
	public final static String dir_unproc = "unproc";
	private final static String dir = "metadata";
	private static void createDir() {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(dir + "/" + dir_proc);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(dir + "/" + dir_unproc);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	private final static String fileName = "metadocs";
	public static void saveMetaDoc(ArrayList<MetaDoc> metadocs, String to, boolean append) {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "/"+ to + "/" + fileName, append));
			for (MetaDoc metadoc: metadocs) {
				bw.write(metadoc.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<MetaDoc> loadMetaDoc(String from) {

		ArrayList<MetaDoc> metadocs = new ArrayList<MetaDoc>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(dir + "/" + from + "/" + fileName));
			while (br.ready()) {
				metadocs.add(new MetaDoc(br.readLine()));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return metadocs;
	}

	public static void main(String... args) {
		ArrayList<MetaDoc> metadocs = new ArrayList<MetaDoc>();
//		metadocs.add(new MetaDoc("google.com",10));
//		metadocs.add(new MetaDoc("facebook.com",20));
//		saveMetaDoc(metadocs);
		metadocs = loadMetaDoc(MetaDocManager.dir_unproc);
		metadocs.forEach((value) -> {
			System.out.println(value.getUrl() + "," +value.getSize());
		});
	}
}
