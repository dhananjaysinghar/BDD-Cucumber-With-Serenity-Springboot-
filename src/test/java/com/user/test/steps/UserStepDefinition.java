package com.user.test.steps;

import com.user.test.utils.TestAssertions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

@RequiredArgsConstructor
@ContextConfiguration(locations = "classpath:cucumber.xml")
public class UserStepDefinition {

    private Response userGetResponse;

    @Given("I create an user with id {string}, name {string} and address {string}")
    public void saveUser(String id, String name, String address) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("address", address);
        FilterableRequestSpecification filterableRequestSpecification =
                (FilterableRequestSpecification) SerenityRest.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .baseUri(System.getProperty("SERVICE_BASE_PATH"));

        filterableRequestSpecification

                .path("/users")
                .body(data);

        Response response = filterableRequestSpecification
                .log().all().when()
                .post(filterableRequestSpecification.getDerivedPath());
        TestAssertions.assertIsTrue("Response code do not match", "Response Code",
                "201", String.valueOf(response.getStatusCode()));
    }

    @When("I search for that user by the id {string}")
    public void iSearchForThatUserByTheId(String id) {
        FilterableRequestSpecification filterableRequestSpecification =
                (FilterableRequestSpecification) SerenityRest.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .baseUri(System.getProperty("SERVICE_BASE_PATH"));
        userGetResponse = filterableRequestSpecification.path("/users/"+id)
                .log().all().when().get(filterableRequestSpecification.getDerivedPath());
        TestAssertions.assertIsTrue("Response code do not match", "Response Code",
                "200", String.valueOf(userGetResponse.getStatusCode()));
    }

    @Then("I should find at least one result with id {string}")
    public void iShouldFindAtLeastOneResult(String id) {
        Map responseData = userGetResponse.prettyPeek().as(Map.class);
        TestAssertions.assertIsTrue("Data not matching", "UserId",
                id, String.valueOf(responseData.get("id")));
    }
}
