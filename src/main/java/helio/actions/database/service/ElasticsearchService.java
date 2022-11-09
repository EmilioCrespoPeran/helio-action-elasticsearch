package helio.actions.database.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.util.RawValue;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import helio.actions.database.exceptions.ElasticsearchException;
import helio.actions.database.model.ElasticsearchParameters;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        } catch (Exception e) {
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
                    .withJson(json));

            waitOperation(1);

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
            BulkRequest request = createBulkRequest(parameters.getIndex(), data);

            waitOperation(1);

            BulkResponse response = client.bulk(request);
            result = processBulkResponse(response, true);
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
            BulkRequest request = createBulkRequest(parameters.getIndex(), data);

            waitOperation(1);

            BulkResponse response = client.bulk(request);
            result = processBulkResponse(response, false);
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

            waitOperation(1);

            result = client.deleteByQuery(DeleteByQueryRequest.of(b -> b
                    .index(parameters.getIndex())
                    .withJson(json))).deleted().toString();
        } catch (Exception e) {
            throw new ElasticsearchException(e.getMessage());
        } finally {
            client.shutdown();
        }

        return result;
    }

    private BulkRequest createBulkRequest(String index, String data) {
        JsonElement jsonData = new Gson().fromJson(data, JsonElement.class);
        BulkRequest.Builder br = new BulkRequest.Builder();
        if (jsonData.isJsonArray()) {
            for (JsonElement json : jsonData.getAsJsonArray()) {
                br.operations(op -> op.index(idx -> idx
                        .index(index)
                        .id(json.getAsJsonObject().has("id") ? json.getAsJsonObject().get("id").getAsString() : null)
                        .document(new RawValue(json.getAsJsonObject().toString()))));
            }
        }
        else {
            br.operations(op -> op.index(idx -> idx
                .index(index)
                .id(jsonData.getAsJsonObject().has("id") ? jsonData.getAsJsonObject().get("id").getAsString() : null)
                .document(new RawValue(jsonData.getAsJsonObject().toString()))));
        }

        return br.build();
    }

    private String processBulkResponse(BulkResponse response, boolean isCreation) {
        String result = isCreation ? "created" : "updated";
        if (response.errors()) {
            result = response.items().stream()
                    .filter(p -> p.error() != null)
                    .map(m -> m.error().reason())
                    .collect(Collectors.joining(","));
        }

        return result;
    }

    /**
     * Waits one second before executing query because Elasticsearch save data in
     * async mode.
     * Only applied for query, update and delete operations
     * 
     * @param seconds
     */
    private void waitOperation(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
        }
    }

}
