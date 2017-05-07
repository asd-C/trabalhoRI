package entity.coletor;

public class MetaDoc {
	private String url;
	private int size; // numero total ocorrencia das entidades
	public MetaDoc(String url, int size) {
		super();
		this.url = url;
		this.size = size;
	}
	public MetaDoc(String metadoc) {
		String[] tmp = metadoc.split(",");
		this.url = tmp[0];
		this.size = Integer.parseInt(tmp[1]);
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
	public String toString() {
		return this.url + "," + this.size;
	}
}
