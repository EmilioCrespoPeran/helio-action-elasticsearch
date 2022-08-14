package controller;

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.gson.Gson;
import exceptions.ElasticsearchConfigurationException;
import exceptions.ElasticsearchException;
import model.ElasticsearchParameters;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

/**
 * Main controller for Elasticsearch operations.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchController {

	private static ElasticsearchController singleton = null;

	private ElasticsearchController() {}
	
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
		ElasticsearchClient client = createClient(parameters);
		String results = null;

		if (parameters.getOperation().equalsIgnoreCase("query")) {
			results = query(client, parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("create")) {
			results = create(client, parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("update")) {
			results = update(client, parameters, data);
		}
		else if (parameters.getOperation().equalsIgnoreCase("delete")) {
			results = delete(client, parameters);
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

	private ElasticsearchClient createClient(ElasticsearchParameters parameters) throws ElasticsearchException {
		ElasticsearchClient client = null;

		try {
			URL url = new URL(parameters.getHost());
			String hostname = url.getHost();
			Integer port = url.getPort() != -1 ? url.getPort() : 9200;

			RestClient restClient = RestClient.builder(new HttpHost(hostname, port)).build();
			ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
			client = new ElasticsearchClient(transport);
		}
		catch (Exception e) {
			// An error cause the client creation
		}

		if (client == null) {
			throw new ElasticsearchException("Cannot connect to " + parameters.getHost());
		}

		return client;
	}
	
	private String query(ElasticsearchClient client, ElasticsearchParameters parameters, String data) throws ElasticsearchException {
		Reader json = new StringReader(data);
		SearchRequest request = SearchRequest.of(b -> b
				.index(parameters.getIndex())
				.withJson(json)
		);

		String result;

		try {
			SearchResponse response = client.search(request, String.class);
			result = new Gson().toJson(response.hits().hits());
		}
		catch (Exception e) {
			throw new ElasticsearchException(e.getMessage());
		}

		return result;
	}

	private String create(ElasticsearchClient client, ElasticsearchParameters parameters, String data) throws ElasticsearchException {
		Reader json = new StringReader(data);
		IndexRequest<JsonData> request = IndexRequest.of(b -> b
				.index(parameters.getIndex())
				.withJson(json)
		);

		String result;

		try {
			result = client.index(request).result().jsonValue();
		}
		catch (Exception e) {
			throw new ElasticsearchException(e.getMessage());
		}

		return result;
	}

	private String update(ElasticsearchClient client, ElasticsearchParameters parameters, String data) throws ElasticsearchException {
		Reader json = new StringReader(data);
		UpdateRequest request = UpdateRequest.of(b -> b
				.index(parameters.getIndex())
				.id(parameters.getDocument())
				.withJson(json)
		);

		String result;

		try {
			result = client.update(request, String.class).result().jsonValue();
		}
		catch (Exception e) {
			throw new ElasticsearchException(e.getMessage());
		}

		return result;
	}

	private String delete(ElasticsearchClient client, ElasticsearchParameters parameters) throws ElasticsearchException {
		DeleteRequest request = DeleteRequest.of(b -> b
				.index(parameters.getIndex())
				.id(parameters.getDocument())
		);

		String result;

		try {
			result = client.delete(request).result().jsonValue();
		}
		catch (Exception e) {
			throw new ElasticsearchException(e.getMessage());
		}

		return result;
	}
	
}
