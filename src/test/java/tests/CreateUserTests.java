package tests;

import base.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests for POST /users endpoint (creating a new user).
 */
public class CreateUserTests extends BaseTest {

    /**
     * Provides multiple name/job combinations to test user creation with.
     * Each row here = one full run of the test method below.
     */
    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][] {
                { "Ahmed", "QA Engineer" },
                { "Sara", "Backend Developer" },
                { "Omar", "Product Manager" }
        };
    }

    @Test(dataProvider = "userData")
    public void createUser_shouldReturnStatus201(String name, String job) {
        given()
                .spec(baseSpec())
                .body("{\"name\": \"" + name + "\", \"job\": \"" + job + "\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(name))
                .body("job", equalTo(job));
    }
}