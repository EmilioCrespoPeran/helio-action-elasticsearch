package service;

import helio.actions.database.exceptions.ElasticsearchException;
import helio.actions.database.model.ElasticsearchParameters;
import helio.actions.database.service.ElasticsearchService;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.MockupModel;
import utils.TestUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ElasticsearchQueryTests {

    private static ElasticsearchService service = new ElasticsearchService();

    @BeforeClass
    public static void setup() throws ElasticsearchException {
        ElasticsearchParameters parametersDelete = TestUtils.deleteParameters();
        String query = TestUtils.findAll();

        ElasticsearchParameters parametersCreate = TestUtils.createParameters();
        MockupModel model1 = new MockupModel("1234", "pedro", 20);
        MockupModel model2 = new MockupModel("5678", "pedro", 44);
        MockupModel model3 = new MockupModel("4321", "ana", 25);

        service.delete(parametersDelete, query);
        TestUtils.waitSeconds(3);

        service.create(parametersCreate, TestUtils.toJson(model1));
        service.create(parametersCreate, TestUtils.toJson(model2));
        service.create(parametersCreate, TestUtils.toJson(model3));
        TestUtils.waitSeconds(3);
    }

    @Test
    public void test01_GetAllDocuments() throws ElasticsearchException {
        ElasticsearchParameters parametersQuery = TestUtils.queryParameters();
        String query = TestUtils.findAll();

        String response = service.query(parametersQuery, query);
        List<MockupModel> data = TestUtils.toModel(response);

        int numDocumentsExpected = 3;
        assertEquals(numDocumentsExpected, data.size());
    }

    @Test
    public void test02_GetDocumentsByName() throws ElasticsearchException {
        ElasticsearchParameters parametersQuery = TestUtils.queryParameters();
        String query = TestUtils.findByName(new MockupModel(null, "ana", null));

        String response = service.query(parametersQuery, query);
        List<MockupModel> data = TestUtils.toModel(response);

        int numDocumentsExpected = 1;
        assertEquals(numDocumentsExpected, data.size());
    }

    @Test
    public void test03_GetDocumentsWithAgeUpperThan22() throws ElasticsearchException {
        ElasticsearchParameters parametersQuery = TestUtils.queryParameters();
        String query = TestUtils.findByAgeBetween(22, 99);
        System.out.println(query);
        String response = service.query(parametersQuery, query);
        List<MockupModel> data = TestUtils.toModel(response);

        int numDocumentsExpected = 2;
        assertEquals(numDocumentsExpected, data.size());
    }

    @Test
    public void test04_GetEmptyResults() throws ElasticsearchException {
        ElasticsearchParameters parametersQuery = TestUtils.queryParameters();
        String query = TestUtils.findByName(new MockupModel(null, "example", null));

        String response = service.query(parametersQuery, query);
        List<MockupModel> data = TestUtils.toModel(response);

        int numDocumentsExpected = 0;
        assertEquals(numDocumentsExpected, data.size());
    }

}
