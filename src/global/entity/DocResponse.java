package global.entity;

public class DocResponse {
	String url;
	String document;
	
	public DocResponse() {}
	
	public DocResponse(String url, String document) {
		this.url = url;
		this.document = document;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
}
