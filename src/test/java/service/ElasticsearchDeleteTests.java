package service;

import helio.actions.database.exceptions.ElasticsearchException;
import helio.actions.database.model.ElasticsearchParameters;
import helio.actions.database.service.ElasticsearchService;
import org.junit.Before;
import org.junit.Test;
import utils.MockupModel;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

public class ElasticsearchDeleteTests {

    private ElasticsearchService service = new ElasticsearchService();

    @Before
    public void reset() throws ElasticsearchException {
        ElasticsearchParameters parametersDelete = TestUtils.deleteParameters();
        String query = TestUtils.findAll();
        service.delete(parametersDelete, query);
        TestUtils.waitSeconds(5);
    }


    @Test
    public void test01_DeleteAllDocuments() throws ElasticsearchException {
        ElasticsearchParameters parametersCreate = TestUtils.createParameters();
        MockupModel model1 = TestUtils.createModel();
        MockupModel model2 = TestUtils.createModel();

        service.create(parametersCreate, TestUtils.toJson(model1));
        service.create(parametersCreate, TestUtils.toJson(model2));

        TestUtils.waitSeconds(5);

        ElasticsearchParameters parametersDelete = TestUtils.deleteParameters();
        String query = TestUtils.findAll();
        Integer obtained = Integer.parseInt(service.delete(parametersDelete, query));
        Integer expected = 2;

        assertEquals(expected, obtained);
    }

    @Test
    public void test02_DeleteByQuery() throws ElasticsearchException {
        ElasticsearchParameters parametersCreate = TestUtils.createParameters();
        MockupModel model1 = TestUtils.createModel();
        MockupModel model2 = TestUtils.createModel();

        service.create(parametersCreate, TestUtils.toJson(model1));
        service.create(parametersCreate, TestUtils.toJson(model2));

        TestUtils.waitSeconds(5);

        ElasticsearchParameters parametersDelete = TestUtils.deleteParameters();
        String query = TestUtils.findByName(model1);
        Integer obtained = Integer.parseInt(service.delete(parametersDelete, query));
        Integer expected = 1;

        assertEquals(expected, obtained);
    }


}
