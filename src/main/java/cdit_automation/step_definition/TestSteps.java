package cdit_automation.step_definition;

import cdit_automation.models.Address;
import cdit_automation.pages.GooglePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Ignore
public class TestSteps extends AbstractSteps {

    @Autowired
    GooglePage googlePage;

    @Given("^I love (vanilla) ice cream$")
    public void iLoveIceCream(String iceCream) {
    }

    @And("I have {int} of them")
    public void iHaveOfThem(int arg0) {
        
    }

    @And("Each cost ${double}")
    public void eachCost$(double sellingPrice) {
        
    }

    @Then("^My identifier is ([S|T|F|G][0-9]{7}[A-Z])$")
    public void myIdentifierIsHFUUUIHIUH(String identifier) {
    }

    @Given("^the following animals:$")
    public void theFollowingAnimals(List<String> animals) {
        for( int i = 0 ; i < animals.size() ; i++ ) {
            log.info("Animal:" +animals.get(i));
        }
    }

    @Given("^I access the google search engine$")
    public void iAccessTheGoogleSearchEngine() {
        testManager.openBrowser();
        googlePage.visitSearchPage();
    }

    @And("^I type (.*) into the search bar$")
    public void iTypeIntoTheSearchBar(String search_string) {
        log.info("Searching for: "+search_string);
        googlePage.enterSearchKeyWords(search_string);
    }

    @And("^I begin searching$")
    public void iBeginSearching() {
        googlePage.search();
    }

    @And("^I verify that the search is done$")
    public void iVerifyThatTheSearchIsDone() {
        log.info("Verifying search is done");
        googlePage.verifySearchSucceeded();
    }

    @Given("An random address")
    public void anRandomAddress() {
        Address result = addressJacksonObjectMapper.getRandomCondominiumAddress();
        testContext.set("address", result);
        log.info("Result: " + result.toString());
    }

    @Then("address should not be empty")
    public void addressShouldNotBeEmpty() {
        Address address = testContext.get("address");
        Assert.assertNotNull(address.getAddress());
        Assert.assertNotEquals("", address.getAddress());
        Assert.assertNotNull(address.getBlockNo());
        Assert.assertNotEquals("", address.getBlockNo());
        Assert.assertNotNull(address.getBuildingName());
        Assert.assertNotEquals("", address.getBuildingName());
        Assert.assertNotNull(address.getPostal());
        Assert.assertNotEquals("", address.getPostal());
        Assert.assertNotNull(address.getRoadName());
        Assert.assertNotEquals("", address.getRoadName());
    }
}
