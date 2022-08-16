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

	public ElasticsearchParameters() {
		this(null, null, null);
	}

	public ElasticsearchParameters(String operation, String host, String index) {
		this.operation = operation;
		this.host = host;
		this.index = index;
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

}
