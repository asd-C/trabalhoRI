package entity.indexador;

public class IndexDetail {
	private int citation;

	public IndexDetail() {
	}

	public IndexDetail(int citation) {
		this.citation = citation;
	}

	public int getCitation() {
		return citation;
	}

	public void setCitation(int citation) {
		this.citation = citation;
	}
	
	public void increment() {
		citation++;
	}

}
