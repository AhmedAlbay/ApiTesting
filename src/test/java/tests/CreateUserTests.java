package tests;

import base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests for POST /users endpoint (creating a new user).
 */
public class CreateUserTests extends BaseTest {

    @Test
    public void createUser_shouldReturnStatus201() {
        given() .spec(baseSpec())
                .body("{\"name\": \"Ahmed\", \"job\": \"QA Engineer\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Ahmed"))
                .body("job", equalTo("QA Engineer"));
    }
}