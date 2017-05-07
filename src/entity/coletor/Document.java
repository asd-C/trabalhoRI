package entity.coletor;

public class Document {
	private String url;
	private String text;
	private int size;
	
	public Document() {}
	public Document(String url, String text, int size) {
		this.url = url;
		this.text = text;
		this.size = size;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
