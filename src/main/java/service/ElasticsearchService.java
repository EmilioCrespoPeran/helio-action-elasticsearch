package service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.ingest.simulate.Document;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exceptions.ElasticsearchException;
import model.ElasticsearchParameters;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

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

        String result;

        try {
            Reader json = new StringReader(data);
            SearchRequest request = SearchRequest.of(b -> b
                    .index(parameters.getIndex())
                    .withJson(json)
            );

            SearchResponse<JsonData> response = client.search(request, JsonData.class);
            ArrayList responseData = new ArrayList<>();
            Gson gson = new Gson();
            for (Hit<JsonData> h : response.hits().hits()) {
                responseData.add(gson.fromJson(h.source().toString(), JsonObject.class));
            }
            result = new Gson().toJson(responseData);
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

        String result = null;

        try {
            Reader json = new StringReader(data);
            IndexRequest<JsonData> request = IndexRequest.of(b -> b
                    .index(parameters.getIndex())
                    .withJson(json)
            );
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

        String result = null;

        try {
            Reader json = new StringReader(data);
            result = client.updateByQuery(UpdateByQueryRequest.of(b -> b
                    .index(parameters.getIndex())
                    .withJson(json))
            ).deleted().toString();
        }
        catch (Exception e) {
            throw new ElasticsearchException(e.getMessage());
        }
        finally {
            client.shutdown();
        }

        return result;
    }

    public String delete(ElasticsearchParameters parameters, String data) throws ElasticsearchException {
        ElasticsearchClient client = createClient(parameters);

        String result = null;

        try {
            Reader json = new StringReader(data);
            result = client.deleteByQuery(DeleteByQueryRequest.of(b -> b
                        .index(parameters.getIndex())
                        .withJson(json))
            ).deleted().toString();
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
