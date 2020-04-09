package automation.pages;

import automation.exceptions.TestFailException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CdsPage extends AbstractPage {
    public final String loginUrl = "/login";
    public final String LOGIN_BTN_TO_FORM = "#btn_login";
    public final String LOGIN_USERNAME = "#username";
    public final String LOGIN_PASSWORD = "#password";
    public final String LOGIN_BTN = "#kc-login";
    public final String LOGOUT_BTN = "#logout-button";

    public final String DATASOURCE_BTN = "#datasource-button";
    public final String DATASOURCE_POPUP = "//div[@class='cdk-overlay-container']/child::div[@class='cdk-overlay-connected-position-bounding-box']/child::div[@id='cdk-overlay-0']";
    public final Map<String, String> DATASOURCE_SUBLINK = new HashMap<String, String>() {
        {
            put("Files Dashboard", "#file-dashboard-sublink");
            put("Data Enquiry", "#data-enquiry-sublink");
            put("Error Rate", "#error-rate-sublink");
        }
    };
    public final Map<String, String> DATASOURCE_FUNCTIONS = new HashMap<String, String>() {
        {
            put("Files Dashboard", "//div[@class='table-title dark-gray-font' and text()='Files Dashboard']");
            put("Data Enquiry", "//header[@class='header' and text()='Data Enquiry']");
            put("Error Rate", "//header[@class='header' and text()='Error Rate Profile']");
        }
    };

    public void visitLogin() {
        visit("/login");
    }

    public void visit(String url) {
        if ( url ==  null ) {
          url = "";
        }
        url = "http://"+testManager.getTestEnvironment().getDatasourceUiUrl()+":"+testManager.getTestEnvironment().getDatasourceUiPort()+url;
        log.info("Visiting webpage at: "+url);
        testManager.openBrowser().get(url);
        verifyLoginPage();
    }

    public void clickLoginBtn() {
        pageUtils.click_on(LOGIN_BTN);
        verifyLoggedInSuccess();
    }

    public void fillInLoginForm() {
        pageUtils.setText(LOGIN_USERNAME, testManager.getTestEnvironment().getDatasourceUiUsername());
        pageUtils.setText(LOGIN_PASSWORD, testManager.getTestEnvironment().getDatasourceUiPassword());
    }

    public void clickLoginBtnToForm() {
        pageUtils.click_on(LOGIN_BTN_TO_FORM);
        verifyLoginForm();
    }

    public void verifyLoginForm() {
        testAssert.assertTrue(pageUtils.hasElement(LOGIN_USERNAME), "Cds Intranet Login Form - Username field not found!");
        testAssert.assertTrue(pageUtils.hasElement(LOGIN_PASSWORD), "Cds Intranet Login Form - Password field not found!");
    }

    public void verifyLoginPage() {
        testAssert.assertTrue(pageUtils.hasElement(LOGIN_BTN_TO_FORM), "Cds Intranet Login button not found!" );
    }

    public void verifyLoggedInSuccess() {
        boolean isFound = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(LOGOUT_BTN);
            }
        });
        testAssert.assertTrue(isFound, "CPF user did not log in successfully!");
    }

    public void accessDatasourceUI(String datasourceSubLink) {
        pageUtils.click_on(DATASOURCE_BTN);
        testAssert.assertTrue(pageUtils.hasElement(DATASOURCE_POPUP), "Datasource popup from clicking the Datasource button is not displayed!");

        pageUtils.click_on(retrieveDatasourceSublink(datasourceSubLink));
        testAssert.assertTrue(pageUtils.hasElement(retrieveDatasourceFunction(datasourceSubLink)), datasourceSubLink+" Profile page not displayed!");
    }

    public void logOff() {
        pageUtils.click_on(LOGOUT_BTN);
        testAssert.assertTrue(pageUtils.hasElement(LOGIN_BTN_TO_FORM), "Cds Intranet not logged out!" );
    }

    private String retrieveDatasourceFunction(String datasourceSubLink) {
        String cssOrXpath = DATASOURCE_FUNCTIONS.get(datasourceSubLink);
        if ( cssOrXpath == null ) {
            throw new TestFailException("There is no such supported css or xpath value for this datasource sublink option: "+datasourceSubLink);
        }
        return cssOrXpath;
    }

    private String retrieveDatasourceSublink(String datasourceSubLink) {
        String cssOrXpath = DATASOURCE_SUBLINK.get(datasourceSubLink);
        if ( cssOrXpath == null ) {
            throw new TestFailException("There is no such supported css or xpath value for this datasource sublink option: "+datasourceSubLink);
        }
        return cssOrXpath;
    }
}
