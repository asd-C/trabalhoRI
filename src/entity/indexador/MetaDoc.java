package entity.indexador;

public class MetaDoc {
	private String url;
	private int size; // numero total ocorrencia das entidades
	public MetaDoc() {}
	public MetaDoc(String url, int size) {
		this.url = url;
		this.size = size;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
