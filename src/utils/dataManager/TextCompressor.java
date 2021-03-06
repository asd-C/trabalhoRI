package utils.dataManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TextCompressor {
	static final int BUFFER = 2048;

	public static boolean zipFromString(String filepath, String filedata) {

		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(filepath);
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

	public static String unzipToString(String filepath) {
		
		String result = null;
		try {
			FileInputStream fis = new FileInputStream(filepath);
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
			return result;
		} catch (Exception e) {
			return null;
		}
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

	public static void main(String... args) {	}
}
