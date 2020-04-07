package cds_automation.step_definition.datasource;

import cds_automation.TestApplication;
import cds_automation.configuration.datasource.TestEnv;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration( classes = {TestApplication.class}, loader = SpringBootContextLoader.class )
public class SpringContextLoader {
    @Autowired TestEnv testEnv;

    @Before(order = 0)
    public void setupContext() {
        System.out.println("======================Initializing spring context======================");
        System.out.println("Test Environment: "+testEnv.getEnv());
        System.out.println("Test Environment details: "+testEnv.toString());
    }
}
