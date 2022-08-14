package actions;

import com.google.gson.JsonObject;

import controller.ElasticsearchController;
import helio.blueprints.Action;
import helio.blueprints.exceptions.ActionException;
import model.ElasticsearchParameters;

/**
 * A component which executes Elasticsearch operations.
 *
 * This JsonObject configuration must contain the following attributes:
 * 	 + "operation" (String). Values admitted:
 * 	  		- "query". Executes search request.
 * 	  		- "create". Index new documents.
 * 	  		- "update". Update a document. It is mandatory to provide a "doc" parameter too.
 * 	  		- "delete". Remove a document. It is mandatory to provide a "doc" parameter too.
 * 	 + "host" (String). Elasticsearch node location.
 * 	 + "index" (String). Name of the index to be read or stored.
 * 	 + "doc" (String). Id of the document in the index. This parameter is mandatory for update and delete operation.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchAction implements Action {

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

		if (configuration.has("document")) {
			parameters.setDocument(configuration.get("document").getAsString());
		}
	}

	@Override
	public String run(String values) throws ActionException {
		return ElasticsearchController.execute(parameters, values);
	}

}
