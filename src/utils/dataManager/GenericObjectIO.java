package utils.dataManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.dataManager.entity.AbstractGenericObject;
import utils.dataManager.entity.GenericObject;

public class GenericObjectIO {

	static {
		createDir();
	}
	private final static String dir = "genericObject";
	private static void createDir() {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	private String fileName;
	public GenericObjectIO(String fileName) {
		this.fileName = fileName;
	}
	
	public void save(List<AbstractGenericObject> objects) {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "/" + fileName));
			for (AbstractGenericObject object: objects) {
				bw.write(object.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<AbstractGenericObject> load() {

		ArrayList<AbstractGenericObject> objects = new ArrayList<AbstractGenericObject>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(dir + "/" + fileName));
			while (br.ready()) {
				objects.add(new GenericObject(br.readLine()));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objects;
	}
}
