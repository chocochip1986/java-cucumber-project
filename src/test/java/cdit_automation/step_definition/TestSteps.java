package cdit_automation.step_definition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Ignore;

import java.util.List;

@Ignore
public class TestSteps extends AbstractSteps {
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

    @Given("I access the google search engine")
    public void iAccessTheGoogleSearchEngine() {
        testManager.getDriverManager().visit("https://www.google.com.sg/");
    }

    @And("I type {string} into the search bar")
    public void iTypeIntoTheSearchBar(String search_string) {
        System.out.println("Searching for: "+search_string);
    }
}
