package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helio.model.ElasticsearchParameters;

import java.lang.reflect.Type;
import java.util.List;
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
        model.setId(UUID.randomUUID().toString());
        model.setName(UUID.randomUUID().toString());
        model.setAge((int) System.currentTimeMillis() % 50 + 10);
        return model;
    }

    public static String toJson(MockupModel model) {
        return new Gson().toJson(model);
    }

    public static List<MockupModel> toModel(String json) {
        Type founderListType = new TypeToken<List<MockupModel>>(){}.getType();

        return new Gson().fromJson(json, founderListType);
    }

    public static String findAll() {
        return "{" +
                    "\"query\": {" +
                        "\"match_all\": {}" +
                    "}" +
                "}";
    }

    public static String findById(MockupModel model) {
        return "{" +
                    "\"query\": {" +
                        "\"match\": { \"id\": \"" + model.getId() + "\" }" +
                    "}" +
                "}";
    }

    public static String findByName(MockupModel model) {
        return "{" +
                    "\"query\": {" +
                        "\"match\": { \"name\": \"" + model.getName() + "\" }" +
                    "}" +
               "}";
    }

    public static String findByAgeBetween(int grather, int less) {
        return "{" +
                    "\"query\": {" +
                        "\"range\": { \"age\": { \"gte\": " + grather + ", \"lte\": " + less + " } }" +
                    "}" +
                "}";
    }


    public static void waitSeconds(int n) {
        try {
            Thread.sleep(n * 1000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
