package tests;


import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class FirstApiTest {
    /**
     * Verifies that fetching an existing user (id = 2) returns status code 200.
     */
    @Test
    public void getSingleUser_returnStatus200(

            ){
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200);
    }
}
