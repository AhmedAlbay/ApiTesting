package tests;

import base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests for GET /users endpoint (positive and negative cases).
 * Note: a test for "missing API key returns 403" was removed —
 * ReqRes's legacy /api/users endpoint has inconsistent enforcement
 * of the key requirement (A/B testing on their end), making that
 * scenario unreliable to test against this real external service.
 */
public class GetUsersTests extends BaseTest {

    @Test
    public void getSingleUser_shouldReturnStatus200() {
        given()
                .spec(baseSpec())
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", containsString("@"));
    }

    @Test
    public void getSingleUser_notFound_shouldReturnStatus404() {
        given()
                .spec(baseSpec())
                .when()
                .get("https://reqres.in/api/users/999")
                .then()
                .statusCode(404);
    }
}