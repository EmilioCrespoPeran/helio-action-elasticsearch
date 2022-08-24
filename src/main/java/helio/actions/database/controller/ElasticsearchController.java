package helio.actions.database.controller;

import helio.actions.database.exceptions.ElasticsearchConfigurationException;
import helio.actions.database.exceptions.ElasticsearchException;
import helio.actions.database.model.ElasticsearchParameters;
import helio.actions.database.service.ElasticsearchService;

/**
 * Main controller for Elasticsearch operations.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchController {

	private static ElasticsearchService service = new ElasticsearchService();


	public static String execute(ElasticsearchParameters parameters, String data) throws ElasticsearchConfigurationException, ElasticsearchException {
		// Validates parameters and data incomming
		validate(parameters, data);
		
		// Execute the operation
		String results = null;

		if (parameters.getOperation().equalsIgnoreCase("query")) {
			results = service.query(parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("create")) {
			results = service.create(parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("update")) {
			results = service.update(parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("delete")) {
			results = service.delete(parameters, data);
		}

		return results;
	}

	private static void validate(ElasticsearchParameters parameters, String data) throws ElasticsearchConfigurationException {
		// Validates elasticsearch configuration
		ElasticsearchValidationParameterController.operation(parameters.getOperation());
		ElasticsearchValidationParameterController.host(parameters.getHost());
		ElasticsearchValidationParameterController.index(parameters.getIndex());

		// Validates input data
		ElasticsearchValidationParameterController.data(parameters.getOperation(), data);
	}

}
