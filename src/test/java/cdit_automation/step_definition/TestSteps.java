package cdit_automation.step_definition;

import cdit_automation.configuration.TestManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;

public class TestSteps {
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
