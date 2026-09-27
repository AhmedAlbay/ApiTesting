package tests;


import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class FirstApiTest {

    /**
     * Runs once before any test in this class.
     * Sets up shared request settings (headers, content type)
     * so every test doesn't have to repeat them.
     */
    @BeforeClass
    public void setup() {

        // Build a reusable request specification with the API key and content type
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .addHeader("x-api-key", ConfigManager.getApiKey())   // read the key from config.properties, not hardcoded
                .setContentType("application/json")                  // applies to every request automatically
                .build();

        // Tell RestAssured to use this spec for every request in this class
        RestAssured.requestSpecification = requestSpec;
    }

    /**
     * Verifies that fetching an existing user (id = 2) returns status code 200.
     */
    @Test
    public void getSingleUser_returnStatus200(

    ) {
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", containsString("@"));
    }

    @Test
    public void createUser_shouldReturnStatus201() {
        given()
                .contentType("application/json")
                .body("{\"name\": \"Ahmed\",\"job\": \"QA Engineer\"}")
                .when().post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Ahmed"))
                .body("job", equalTo("QA Engineer"));
    }
    /**
     * Verifies that updating an existing user (id = 2) returns status code 200.
     */
    @Test
    public void updateUser_shouldReturnStatus200(){
    given()
            .body("{\"name\": \"Ahmed\", \"job\": \"Senior QA Engineer\"}")
        .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("job", equalTo("Senior QA Engineer"));
    }
    /**
     * Verifies that deleting an existing user (id = 2) returns status code 204.
     */
    @Test
    public void deleteUser_shouldReturnStatus204(){
        given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204)
    ;}
    /**
     * Negative test: requesting a user that doesn't exist should return 404.
     */
    @Test
    public void getSingleUser_notFound_shouldReturnStatus404() {
        given()
                .when()
                .get("https://reqres.in/api/users/999")
                .then()
                .statusCode(404);
    }
    /**
     * Negative test: requesting a user without the API key should return 401.
     */
    @Test
    public void getSingleUser_withoutApiKey_shouldReturnStatus403() {
        given()
                .header("x-api-key", "")   // override the key with an empty value
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(403);
    }
}
