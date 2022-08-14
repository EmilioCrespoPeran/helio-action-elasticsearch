package utils;

import com.google.gson.Gson;
import model.ElasticsearchParameters;

import java.util.UUID;

public class TestUtils {

    public static ElasticsearchParameters queryParameters() {
        return new ElasticsearchParameters("query", "http://localhost:9200", "mockup");
    }

    public static ElasticsearchParameters createParameters() {
        return new ElasticsearchParameters("create", "http://localhost:9200", "mockup");
    }

    public static ElasticsearchParameters updateParameters() {
        return new ElasticsearchParameters("update", "http://localhost:9200", "mockup");
    }

    public static ElasticsearchParameters deleteParameters() {
        return new ElasticsearchParameters("delete", "http://localhost:9200", "mockup");
    }

    public static MockupModel createModel() {
        MockupModel model = new MockupModel();
        model.setName(UUID.randomUUID().toString());
        model.setAge((int) System.currentTimeMillis() % 50 + 10);
        return model;
    }

    public static String toJson(MockupModel model) {
        return new Gson().toJson(model);
    }

    public static String findAll() {
        return "{" +
                    "\"query\": {" +
                        "\"match_all\": {}" +
                    "}" +
                "}";
    }

}
