package global;

import java.io.File;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.ObjectMapper;

import coletor.Classifier;
import entity.coletor.SeedsManager;
import entity.indexador.InvertedIndexManager;
import entity.indexador.MetaDocManager;
import utils.dataManager.TextCompressor;

public class Global {
	
	private static long size_all_file_with_compression;
	private static long size_all_file_without_compression;

	public synchronized static long getSize_all_file_with_compression() {
		return size_all_file_with_compression;
	}

	public synchronized static void setSize_all_file_with_compression(long size_all_file_with_compression) {
		Global.size_all_file_with_compression = size_all_file_with_compression;
	}

	public synchronized static long getSize_all_file_without_compression() {
		return size_all_file_without_compression;
	}

	public synchronized static void setSize_all_file_without_compression(long size_all_file_without_compression) {
		Global.size_all_file_without_compression = size_all_file_without_compression;
	}
	
	public synchronized static void addSize_all_file_without_compression(long increment) {
		Global.size_all_file_without_compression += increment;
	}

	public static void sumSizeWithoutCompression() {
		File dir = new File(pathFormat(dir_root, dir_document));
		File[] files = dir.listFiles();
		long sum = 0;
		
		for (File file : files) {
			sum += TextCompressor.unzipToString(file.getAbsolutePath()).length();
		}
		
		System.out.println(sum);
	}
	
	public static void sumSizeWithCompression() {
		File dir = new File(pathFormat(dir_root, dir_document));
		File[] files = dir.listFiles();
		long sum = 0;
		
		for (File file : files) {
			sum += file.length();
		}
		
		setSize_all_file_with_compression(sum);
//		System.out.println(sum);
	}
	
	public static void logSizeOfDocument() {
		sumSizeWithCompression();
		log("Size with compression: " + getSize_all_file_with_compression());
		log("Size without compression: " + getSize_all_file_without_compression());
	}
	
	public static ObjectMapper objectMapper;
	public static Classifier classifier;
	public static InvertedIndexManager invertedIndexManager;
	public static MetaDocManager metaDocManager;
	public static SeedsManager seedsManager;

	public static void loadData() {
		log("Preparing datas...");

		createDir();

		size_all_file_with_compression = 0l;
		size_all_file_without_compression = 0l;
		
		objectMapper = new ObjectMapper();
		classifier = new Classifier();
		invertedIndexManager = new InvertedIndexManager();
		metaDocManager = new MetaDocManager();
		seedsManager = new SeedsManager();
	}

	public final static String dir_root = "data";

	public final static String dir_document = "document";
	public final static String dir_metadoc = "metadoc";
	public final static String dir_seed = "seed";
	public final static String dir_inverted_index = "inverted_index";

	public final static String file_metadoc = "metadoc";
	public final static String file_unvisited_seed = "unvisited_seed";
	public final static String file_visited_seed = "visited_seed";
	public final static String file_inverted_index = "inverted_index";

	private static void createDir() {
		File file = new File(dir_root);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(pathFormat(dir_root, dir_document));
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(pathFormat(dir_root, dir_metadoc));
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(pathFormat(dir_root, dir_seed));
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(pathFormat(dir_root, dir_inverted_index));
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static String pathFormat(String... args) {
		String path = args[0];

		for (int i = 1; i < args.length; i++) {
			path += "/" + args[i];
		}

		return path;
	}

	public static String digestSHA1(String in) {
		return DigestUtils.sha1Hex(in);
	}

	public static void log(String info) {
		System.out.println(info + "\n");
	}

}
