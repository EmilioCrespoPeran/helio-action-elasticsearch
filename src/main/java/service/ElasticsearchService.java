package service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.gson.Gson;
import exceptions.ElasticsearchException;
import model.ElasticsearchParameters;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

/**
 * Service provided for executing Elasticsearch operations.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchService {

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




    public String query(ElasticsearchParameters parameters, String data) throws ElasticsearchException {
        ElasticsearchClient client = createClient(parameters);

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
        finally {
            client.shutdown();
        }

        return result;
    }

    public String create(ElasticsearchParameters parameters, String data) throws ElasticsearchException {
        ElasticsearchClient client = createClient(parameters);

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
        finally {
            client.shutdown();
        }

        return result;
    }

    public String update(ElasticsearchParameters parameters, String data) throws ElasticsearchException {
        ElasticsearchClient client = createClient(parameters);

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
        finally {
            client.shutdown();
        }

        return result;
    }

    public String delete(ElasticsearchParameters parameters) throws ElasticsearchException {
        ElasticsearchClient client = createClient(parameters);

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
        finally {
            client.shutdown();
        }

        return result;
    }

}
