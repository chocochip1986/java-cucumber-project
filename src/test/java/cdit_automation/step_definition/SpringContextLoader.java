package cdit_automation.step_definition;

import cdit_automation.TestApplication;
import io.cucumber.java.Before;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration( classes = {TestApplication.class}, loader = SpringBootContextLoader.class )
public class SpringContextLoader {
    @Before(order = 0)
    public void setupContext() {
        System.out.println("======================Initializing spring context======================");
    }
}
