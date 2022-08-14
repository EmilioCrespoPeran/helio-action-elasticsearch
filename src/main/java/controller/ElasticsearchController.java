package controller;

import exceptions.ElasticsearchConfigurationException;
import exceptions.ElasticsearchException;
import model.ElasticsearchParameters;
import service.ElasticsearchService;

/**
 * Main controller for Elasticsearch operations.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchController {

	private static ElasticsearchController singleton = null;
	private ElasticsearchService service;

	private ElasticsearchController() {
		this.service = new ElasticsearchService();
	}
	
	public static String execute(ElasticsearchParameters parameters, String data) throws ElasticsearchConfigurationException, ElasticsearchException {
		if (singleton == null) {
			singleton = new ElasticsearchController();
		}
		return singleton.process(parameters, data);
	}
	
	private String process(ElasticsearchParameters parameters, String data) throws ElasticsearchConfigurationException, ElasticsearchException {
		// Validates parameters and data incomming
		validate(parameters, data);
		
		// Execute the operation
		String results = null;

		if (parameters.getOperation().equalsIgnoreCase("query")) {
			results = this.service.query(parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("create")) {
			results = this.service.create(parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("update")) {
			results = this.service.update(parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("delete")) {
			results = this.service.delete(parameters);
		}

		return results;
	}

	private void validate(ElasticsearchParameters parameters, String data) throws ElasticsearchConfigurationException {
		// Validates elasticsearch configuration
		ElasticsearchValidationParameterController.operation(parameters.getOperation());
		ElasticsearchValidationParameterController.host(parameters.getHost());
		ElasticsearchValidationParameterController.index(parameters.getIndex());
		ElasticsearchValidationParameterController.document(parameters.getOperation(), parameters.getDocument());

		// Validates input data
		ElasticsearchValidationParameterController.data(parameters.getOperation(), data);
	}

}
