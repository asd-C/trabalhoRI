package global;

import java.util.ArrayList;

import global.entity.DocResponse;

public interface MainInterface {
	public ArrayList<DocResponse> querying(String[] query);
	public ArrayList<DocResponse> querying(String query);
}
