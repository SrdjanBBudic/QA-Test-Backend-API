import io.restassured.RestAssured;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class getCustomer {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://019ee.mocklab.io";
    }

    @Test
    public void getCustomerFieldsPresence() {
        String getCustomer = getCustomerAsString();

        JSONArray customers = rawStringToJsonArray(getCustomer);
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(customer.has("id")).as("Id field is missing").isTrue();
            softly.assertThat(customer.has("first_name")).as("First Name field is missing").isTrue();
            softly.assertThat(customer.has("last_name")).as("Lase Name field is missing").isTrue();
            softly.assertThat(customer.has("age")).as("Age field is missing").isTrue();
            softly.assertThat(customer.has("created_at")).as("Create At field is missing").isTrue();
            softly.assertThat(customer.has("email")).as("Email should not be present").isFalse();
            softly.assertAll();
        }
    }

    @Test
    public void getCustomerKeysNotNull() {
        String getCustomer = getCustomerAsString();

        JSONArray customers = rawStringToJsonArray(getCustomer);
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(customer.get("id")).as("Id is null").isNotNull();
            softly.assertThat(customer.get("first_name")).as("First Name is null").isNotNull();
            softly.assertThat(customer.get("last_name")).as("Last Name is null").isNotNull();
            softly.assertThat(customer.get("age")).as("Age is null").isNotNull();
            softly.assertThat(customer.get("created_at")).as("Created At is null").isNotNull();
            softly.assertThat(customer.has("email")).as("Email should not be present").isFalse();
            softly.assertAll();
        }
    }

    @Test
    public void getCustomersMatchDB() {
        String getCustomer = getCustomerAsString();

        JSONArray customers = rawStringToJsonArray(getCustomer);
        JSONObject customerOne = customers.getJSONObject(0);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(customerOne.get("id")).as("Id doesn't match the DB").isEqualTo(1);
        softly.assertThat(customerOne.get("first_name")).as("First Name doesn't match the DB").isEqualTo("Inyova");
        softly.assertThat(customerOne.get("last_name")).as("Last Name doesn't match the DB").isEqualTo("Inyova");
        softly.assertThat(customerOne.get("age")).as("Age doesn't match the DB").isEqualTo(18);
        softly.assertThat(customerOne.get("created_at")).as("Created At doesn't match the DB").isEqualTo("2019-03-19");

        JSONObject customerTwo = customers.getJSONObject(1);
        softly.assertThat(customerTwo.get("id")).as("Id doesn't match the DB").isEqualTo(2);
        softly.assertThat(customerTwo.get("first_name")).as("First Name doesn't match the DB").isEqualTo("Foo");
        softly.assertThat(customerTwo.get("last_name")).as("Last Name doesn't match the DB").isEqualTo("Bar");
        softly.assertThat(customerTwo.get("age")).as("Age doesn't match the DB").isEqualTo(40);
        softly.assertThat(customerTwo.get("created_at")).as("Created At doesn't match the DB").isEqualTo("2019-03-19");
        softly.assertAll();
    }

    @Test
    public void getCustomerAuthorizationValid() {
        given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .when().get("customers")
                .then().log().all().assertThat().statusCode(200).extract().asString();
    }

    @Test
    public void getCustomerAuthorizationInvalid() {
        given().header("Authorization", "invalidAuthorization")
                .when().get("customers")
                .then().log().all().assertThat().statusCode(401);
    }

    @Test
    public void getCustomersAvailable() {
        String getCustomer = getCustomerAsString();

        JSONArray customers = rawStringToJsonArray(getCustomer);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(customers.length()).as("Number of customers is 0").isNotNull();
        softly.assertThat(customers.length()).as("Number of customers doesn't match DB").isEqualTo(2);
        softly.assertAll();
    }

    private String getCustomerAsString() {
        String getCustomer = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .when().get("customers")
                .then().log().all().assertThat().statusCode(200).extract().asString();
        return getCustomer;
    }

    private static JSONArray rawStringToJsonArray(String input) {
        JSONArray result = null;
        try {
            result = new JSONArray(input);
        } catch (JSONException e) {
            System.err.println(e);
        }

        return result;
    }
}
