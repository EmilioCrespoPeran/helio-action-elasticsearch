package E2E;


import helio.actions.database.exceptions.ElasticsearchException;
import helio.blueprints.components.ComponentType;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.actions.database.model.ElasticsearchParameters;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import helio.actions.database.service.ElasticsearchService;
import utils.E2EUtils;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElasticsearchActionIntegrationTests {

    private ElasticsearchService service = new ElasticsearchService();

    @BeforeClass
    public static void setup() throws ExtensionNotFoundException {
        Components.registerAndLoad(
            "https://github.com/EmilioCrespoPeran/helio-action-elasticsearch/releases/download/v0.1.0/helio-action-elasticsearch-0.1.0.jar",
            "helio.actions.database.ElasticsearchAction",
            ComponentType.ACTION);
    }

    @Before
    public void reset() throws ElasticsearchException {
        ElasticsearchParameters parametersDelete = TestUtils.deleteParameters();
        String query = TestUtils.findAll();
        service.delete(parametersDelete, query);
        TestUtils.waitSeconds(5);
    }


    @Test
    public void test01_CreateMockupData() {
        try {
            String obtained = E2EUtils.executeTestWithTemplate(E2EUtils.DIR_RESOURCES + "test01.txt").strip();
            String expected = "created";
            assertEquals(expected, obtained);
        }
        catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

}
