package service;

import helio.actions.database.exceptions.ElasticsearchException;
import helio.actions.database.model.ElasticsearchParameters;
import helio.actions.database.service.ElasticsearchService;
import org.junit.Test;
import utils.MockupModel;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class ElasticsearchCreateTests {

    private ElasticsearchService service = new ElasticsearchService();

    @Test
    public void test01_CreateSimpleData() throws ElasticsearchException {
        ElasticsearchParameters parameters = TestUtils.createParameters();
        MockupModel model = TestUtils.createModel();

        String obtained = service.create(parameters, TestUtils.toJson(model));
        String expected = "created";

        assertEquals(expected, obtained);
    }

    @Test
    public void test02_CreateDataWithoutAllFields() throws ElasticsearchException {
        ElasticsearchParameters parameters = TestUtils.createParameters();
        MockupModel model = TestUtils.createModel();
        model.setAge(null);

        String obtained = service.create(parameters, TestUtils.toJson(model));
        String expected = "created";

        assertEquals(expected, obtained);
    }


    @Test
    public void test03_CreateMultipleData() throws ElasticsearchException {
        ElasticsearchParameters parameters = TestUtils.createParameters();
        List<MockupModel> data = List.of(TestUtils.createModel(), TestUtils.createModel(), TestUtils.createModel());

        String obtained = service.create(parameters, TestUtils.toJson(data));
        String expected = "created";

        assertEquals(expected, obtained);
    }

}
