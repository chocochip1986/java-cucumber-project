package cdit_automation.step_definition;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;

public class TestSteps {
    @Before
    public void before(Scenario scenario) {
        System.out.println("======================================================");
        System.out.println("Starting - " +scenario.getName());
        System.out.println("======================================================");
    }

    @After
    public void after(Scenario scenario) {
        System.out.println("======================================================");
        System.out.println("Done - " +scenario.getName()+ " => Status: "+scenario.getStatus());
        System.out.println("======================================================");
    }

    @Given("I love {string} ice cream")
    public void iLoveIceCream(String iceCream) {
    }

    @And("I have {int} of them")
    public void iHaveOfThem(int arg0) {
        
    }

    @And("Each cost ${double}")
    public void eachCost$(double sellingPrice) {
        
    }

    @Then("My identifier is {}")
    public void myIdentifierIsHFUUUIHIUH(String trash) {
    }

    @Given("the following animals:")
    public void theFollowingAnimals(List<String> animals) {
        for( int i = 0 ; i < animals.size() ; i++ ) {
            System.out.println("Animal: "+animals.get(i));
        }
    }
}
