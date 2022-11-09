package service;

import helio.actions.database.exceptions.ElasticsearchException;
import helio.actions.database.model.ElasticsearchParameters;
import helio.actions.database.service.ElasticsearchService;

import org.junit.Test;
import utils.MockupModel;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class ElasticsearchUpdateTests {

    private ElasticsearchService service = new ElasticsearchService();

    @Test
    public void test01_UpdateSingleDocument() throws ElasticsearchException {
        ElasticsearchParameters parametersQuery = TestUtils.queryParameters();
        ElasticsearchParameters parametersCreate = TestUtils.createParameters();
        ElasticsearchParameters parametersUpdate = TestUtils.updateParameters();

        List<MockupModel> data = List.of(TestUtils.createModel(), TestUtils.createModel(), TestUtils.createModel());
        service.create(parametersCreate, TestUtils.toJson(data));

        MockupModel target = data.get(0);
        target.setAge(80);
        String result = service.update(parametersUpdate, TestUtils.toJson(target));

        String query = TestUtils.findById(target);
        List<MockupModel> obtained = TestUtils.toModel(service.query(parametersQuery, query));

        assertEquals(1, obtained.size());
        assertEquals(80, obtained.get(0).getAge().intValue());
        assertEquals("updated", result);
    }

    @Test
    public void test02_UpdateMultipleDocuments() throws ElasticsearchException {
        ElasticsearchParameters parametersQuery = TestUtils.queryParameters();
        ElasticsearchParameters parametersCreate = TestUtils.createParameters();
        ElasticsearchParameters parametersUpdate = TestUtils.updateParameters();

        List<MockupModel> data = List.of(TestUtils.createModel(), TestUtils.createModel(), TestUtils.createModel());
        service.create(parametersCreate, TestUtils.toJson(data));

        MockupModel target1 = data.get(0);
        target1.setAge(22);
        MockupModel target2 = data.get(1);
        target2.setAge(25);
        String result = service.update(parametersUpdate, TestUtils.toJson(List.of(target1, target2)));

        String query = TestUtils.findByAgeBetween(20, 30);
        List<MockupModel> obtained = TestUtils.toModel(service.query(parametersQuery, query));

        assertEquals(2, obtained.size());
        assertEquals(22, obtained.get(0).getAge().intValue());
        assertEquals(25, obtained.get(1).getAge().intValue());
        assertEquals("updated", result);
    }

}
