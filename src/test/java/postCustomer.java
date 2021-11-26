import io.restassured.RestAssured;
import org.apache.commons.validator.routines.EmailValidator;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class postCustomer {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://019ee.mocklab.io";
    }

    @Test
    public void postValidCustomer() {
        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("first_name", "Inyova");
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers")
                .then().log().all().assertThat().statusCode(201);
    }

    @Test
    public void postTwinCustomer() {
        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("first_name", "Inyova");
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers")
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    public void postEmptyCustomer() {
        JSONObject customer = new JSONObject();
        given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers")
                .then().log().all().assertThat().statusCode(400);

        given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .when().post("customers")
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    public void postValidCustomerInvalidAuthorization() {
        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("first_name", "Inyova");
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        given().header("Authorization", "invalidAuthorizationTest==")
                .body(customer)
                .when().post("customers")
                .then().log().all().assertThat().statusCode(401);
    }

    @Test
    public void postCustomerInvalidId() {
        SoftAssertions softly = new SoftAssertions();

        JSONObject customer = new JSONObject();
        customer.put("first_name", "Inyova");
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        Integer postNoId = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postNoId).as("Id field is missing").isEqualTo(400);

        customer.put("id", "3");
        Integer postIdAsString = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postIdAsString).as("Id can't be a String").isEqualTo(400);

        customer.put("id", 4.5);
        Integer postIdAsFloat = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postIdAsFloat).as("Id can't be a Float number").isEqualTo(400);
        softly.assertAll();
    }

    @Test
    public void postCustomerInvalidFirstName() {
        SoftAssertions softly = new SoftAssertions();

        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        Integer postNoFirstName = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postNoFirstName).as("First Name field is missing").isEqualTo(400);

        customer.put("first_name", 124124);
        Integer postFirstNameAsInteger = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postFirstNameAsInteger).as("First Name can't be an Integer").isEqualTo(400);

        customer.put("first_name", 4.5);
        Integer postFirstNameAsFloat = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postFirstNameAsFloat).as("First Name can't be a Float number").isEqualTo(400);

        customer.put("first_name", "inyova");
        Integer postFirstNameAsLowerCase = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postFirstNameAsLowerCase).as("First Name must be Camel Case").isEqualTo(400);

        customer.put("first_name", "INYOVA");
        Integer postFirstNameAsUpperCase = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postFirstNameAsUpperCase).as("First Name must be Camel Case").isEqualTo(400);

        softly.assertAll();
    }

    @Test
    public void postCustomerInvalidLastName() {
        SoftAssertions softly = new SoftAssertions();

        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("first_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        Integer postNoLastName = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postNoLastName).as("Last Name field is missing").isEqualTo(400);

        customer.put("last_name", 124124);
        Integer postLastNameAsInteger = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postLastNameAsInteger).as("Last Name can't be an Integer").isEqualTo(400);

        customer.put("last_name", 4.5);
        Integer postLastNameAsFloat = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postLastNameAsFloat).as("Last Name can't be a Float number").isEqualTo(400);

        customer.put("last_name", "inyova");
        Integer postLastNameAsLowerCase = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postLastNameAsLowerCase).as("Last Name must be Camel Case").isEqualTo(400);

        customer.put("last_name", "INYOVA");
        Integer postLastNameAsUpperCase = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postLastNameAsUpperCase).as("Last Name must be Camel Case").isEqualTo(400);
        softly.assertAll();
    }

    @Test
    public void postCustomerInvalidEmail() {
        SoftAssertions softly = new SoftAssertions();

        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("first_name", "Inyova");
        customer.put("age", 18);
        customer.put("created_at", "2019-03-19");

        Integer postNoEmail = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postNoEmail).as("Email field is missing").isEqualTo(400);

        customer.put("email", 124124);
        Integer postEmailAsInteger = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postEmailAsInteger).as("Email can't be an Integer").isEqualTo(400);

        customer.put("email", 4.5);
        Integer postEmailAsFloat = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postEmailAsFloat).as("Email can't be a Float number").isEqualTo(400);
        softly.assertAll();
    }

    @Test
    public void postEmailValidation() {
//        I've found two ways to go about it, but we can choose the regex for the other way.

        SoftAssertions softly = new SoftAssertions();
        String email = "inyovainyova.com";
        boolean valid = EmailValidator.getInstance().isValid(email);
        softly.assertThat(valid).as("Email regex is not correct").isTrue();

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        softly.assertThat(matcher.matches()).as("Email regex is not correct").isTrue();
        softly.assertAll();
    }

    @Test
    public void postCustomerInvalidAge() {
        SoftAssertions softly = new SoftAssertions();

        JSONObject customer = new JSONObject();
        customer.put("first_name", "Inyova");
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("id", 3);
        customer.put("created_at", "2019-03-19");

        Integer postNoAge = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postNoAge).as("Age field is missing").isEqualTo(400);

        customer.put("age", "3");
        Integer postAgeAsString = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postAgeAsString).as("Age can't be a String").isEqualTo(400);

        customer.put("age", 4.5);
        Integer postAgeAsFloat = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postAgeAsFloat).as("Age can't be a Float number").isEqualTo(400);
        softly.assertAll();
    }

    @Test
    public void postCustomerInvalidCreatedAt() {
        SoftAssertions softly = new SoftAssertions();

        JSONObject customer = new JSONObject();
        customer.put("id", 3);
        customer.put("first_name", "Inyova");
        customer.put("last_name", "Inyova");
        customer.put("email", "inyova@inyova.com");
        customer.put("age", 18);

        Integer postNoCreatedAt = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postNoCreatedAt).as("Created At field is missing").isEqualTo(400);

        customer.put("created_at", 124124);
        Integer postCreatedAtAsInteger = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postCreatedAtAsInteger).as("Created At can't be an Integer").isEqualTo(400);

        customer.put("created_at", 4.5);
        Integer postCreatedAtAsFloat = given().header("Authorization", "eW92YV90ZXN0OnlvdmFfcGFhc3N3b3JkCg==")
                .body(customer)
                .when().post("customers").then().extract().statusCode();

        softly.assertThat(postCreatedAtAsFloat).as("Created At can't be a Float number").isEqualTo(400);
        softly.assertAll();
    }


}
