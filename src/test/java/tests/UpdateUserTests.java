package tests;

import base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests for PUT /users endpoint (updating an existing user).
 */
public class UpdateUserTests extends BaseTest {

    @Test
    public void updateUser_shouldReturnStatus200() {
        given() .spec(baseSpec())
                .body("{\"name\": \"Ahmed\", \"job\": \"Senior QA Engineer\"}")
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("job", equalTo("Senior QA Engineer"));
    }
}