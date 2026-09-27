package tests;

import base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Tests for DELETE /users endpoint (deleting a user).
 */
public class DeleteUserTests extends BaseTest {

    @Test
    public void deleteUser_shouldReturnStatus204() {
        given() .spec(baseSpec())
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }
}