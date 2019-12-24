package cdit_automation.step_definition;

import cdit_automation.TestApplication;
import cdit_automation.configuration.TestEnv;
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
        setActiveSpringProfile();
        System.out.println("Test Environment: "+testEnv.getEnv());
        System.out.println("Test Environment details: "+testEnv.toString());
    }

    private static void setActiveSpringProfile() {
        if ( System.getProperty("spring.profiles.active") == null ) {
            System.setProperty("spring.profiles.active", "local");
        }
    }
}
