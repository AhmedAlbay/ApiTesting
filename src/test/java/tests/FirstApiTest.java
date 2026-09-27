package tests;


import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class FirstApiTest {
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
}
