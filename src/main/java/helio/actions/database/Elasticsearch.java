package helio.actions.database;

import com.google.gson.JsonObject;

import helio.actions.database.controller.ElasticsearchController;
import helio.blueprints.Action;
import helio.blueprints.exceptions.ActionException;
import helio.actions.database.model.ElasticsearchParameters;

/**
 * A component which executes Elasticsearch operations.
 *
 * This JsonObject configuration must contain the following attributes:
 * 	 + "operation" (String). Values admitted:
 * 	  		- "query". Executes search request.
 * 	  		- "create". Index new documents.
 * 	  		- "update". Update a document.
 * 	  		- "delete". Remove a document.
 * 	 + "host" (String). Elasticsearch node location.
 * 	 + "index" (String). Name of the index to be read or stored.
 *
 * @author Emilio Crespo Perán
 */
public class Elasticsearch implements Action {

	private ElasticsearchParameters parameters = new ElasticsearchParameters();

	@Override
	public void configure(JsonObject configuration) {
		if (configuration == null) {
			return;
		}
		
		if (configuration.has("operation")) {
			parameters.setOperation(configuration.get("operation").getAsString());
		}
		
		if (configuration.has("host")) {
			parameters.setHost(configuration.get("host").getAsString());
		}

		if (configuration.has("index")) {
			parameters.setIndex(configuration.get("index").getAsString());
		}
	}

	@Override
	public String run(String values) throws ActionException {
		return ElasticsearchController.execute(parameters, values);
	}

}
