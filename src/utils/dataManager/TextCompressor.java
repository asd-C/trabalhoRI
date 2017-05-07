package utils.dataManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import entity.coletor.Document;

public class TextCompressor {
	static final int BUFFER = 2048;

	public static boolean zipFromString(String filename, String filedata) {
		
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(filename);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			byte data[] = new byte[BUFFER];
			ByteArrayInputStream bais = new ByteArrayInputStream(filedata.getBytes("UTF-8"));
			origin = new BufferedInputStream(bais, BUFFER);

			out.putNextEntry(new ZipEntry("file"));

			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String unzipToString(String filename) {
		
		String result = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

			result = "";
			while (zis.getNextEntry() != null) {
				
				BufferedReader br = new BufferedReader(new InputStreamReader(zis, "UTF-8"));
				StringBuilder sb = new StringBuilder();
			      String line;
			      while(( line = br.readLine()) != null ) {
			         sb.append( line );
			         sb.append( '\n' );
			      }
				result += sb.toString();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void zipFromFile(String file_out, String file_in) {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(file_out);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			byte data[] = new byte[BUFFER];
			File f = new File(file_in);
			FileInputStream fi = new FileInputStream(f);
			origin = new BufferedInputStream(fi, BUFFER);
			
			out.putNextEntry(new ZipEntry(f.getName()));
			
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unzipToFile(String file_in) {
		try {
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(file_in);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void compress(Document doc, String to, boolean base64) {
		String filename = doc.getUrl();
		if (base64) {
			filename = base64(filename, false);
		} 
		zipFromString(dir + "/" + to + "/" + filename, doc.getText());
		
//		return filename;
	}
	
	public static String base64(String in, boolean decode) {
		if (decode) {
			return new String(Base64.decodeBase64(in.getBytes()));
		} else {
			return new String(Base64.encodeBase64(in.getBytes()));
		}
	}
	
	public static String uncompress(String filename, String from, boolean base64) {
		if (base64) {
			filename = base64(filename, false);
		} 
		return unzipToString(dir + "/" + from + "/" + filename);
	}

	public static ArrayList<Document> rename(File[] files, String from, String to) {
		ArrayList<Document> filesC = new ArrayList<Document>();
		
		for (File f: files) {
			File file = new File(dir + "/" + from + "/" + f.getName());
			String newName = base64(f.getName(), true);
			boolean suc = file.renameTo(new File(dir + "/" + to + "/" + digest(newName)));
			if (suc) filesC.add(new Document(newName, "", 0));
			System.out.println(newName + ": " + file.getName());
		}
		
		return filesC;
	}
	
	public static String digest(String in) {
		return DigestUtils.sha1Hex(in);
	}
	
	static {
		createDir();
	}
	public final static String dir_proc = "proc";
	public final static String dir_unproc = "unproc";
	public final static String dir = "data";
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
	public static void main(String... args) {
//		changeDir("aHR0cHM6Ly9lbi53aWtpcGVkaWEub3JnL3dpa2kvQ2F0ZWdvcnk6TWlzc2luZ19taWRkbGVfb3JfZmlyc3RfbmFtZXM=");
//		changeDir("aHR0cHM6Ly9lbi53aWtpcGVkaWEub3JnL3dpa2kvQ2F0ZWdvcnk6TWlzc2luZ19taWRkbGVfb3JfZmlyc3RfbmFtZXM=");
		//aHR0cHM6Ly9lbi53aWtpcGVkaWEub3JnL3dpa2kvQ2F0ZWdvcnk6TGl2aW5nX3Blb3BsZQ==
//		compress(new Document("https://en.wikipedia.org/wiki/Category:Living_people","hello world",0), TextCompressor.dir_unproc, true);
//		System.out.println(uncompress("https://en.wikipedia.org/wiki/Category:Living_people", dir_unproc, true));
		//		changeDir("google.com");
	}
}
