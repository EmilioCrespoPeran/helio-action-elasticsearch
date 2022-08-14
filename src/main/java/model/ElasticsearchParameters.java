package model;

/**
 * Model for Elasticsearch parameters
 * 
 * @author Emilio Crespo Perán
 */
public class ElasticsearchParameters {

	private String operation;
	private String host;
	private String index;
	private String document;
	
	public ElasticsearchParameters() {
		this(null, null, null, null);
	}
	
	public ElasticsearchParameters(String operation, String host, String index, String document) {
		this.operation = operation;
		this.host = host;
		this.index = index;
		this.document = document;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getIndex() {
		return index;
	}
	
	public void setIndex(String index) {
		this.index = index;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

}
