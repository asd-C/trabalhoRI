package entity.indexador;

public class IndexDoc {
	public String url;
	public int fi;
	
	IndexDoc() {}
	
	public IndexDoc(String url, int fi) {
		this.url = url;
		this.fi = fi;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFi() {
		return fi;
	}

	public void setFi(int fi) {
		this.fi = fi;
	}

}