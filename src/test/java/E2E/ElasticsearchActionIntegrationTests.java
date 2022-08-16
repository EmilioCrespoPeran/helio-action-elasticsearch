package E2E;


import helio.blueprints.components.ComponentType;
import helio.blueprints.components.Components;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.E2EUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElasticsearchActionIntegrationTests {

    @BeforeClass
    public static void setup() {
        Components.register(null, "actions.ElasticsearchAction", ComponentType.ACTION);
    }


    @Test
    public void test01_CreateMockupData() {
        try {
            String obtained = E2EUtils.executeTestWithTemplate(E2EUtils.DIR_RESOURCES + "test01.txt");
            String expected = "created";
            assertEquals(expected, obtained);
        }
        catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }



}
