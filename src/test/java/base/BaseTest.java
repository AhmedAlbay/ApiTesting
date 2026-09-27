package base;

import config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all API test classes.
 * Provides a fresh request spec for each call, and adds a small delay
 * before each test to avoid hitting the free API's rate limit.
 */
public class BaseTest {

    @BeforeMethod
    public void delayBetweenRequests() throws InterruptedException {
        Thread.sleep(500); // wait 0.5 second before each test
    }

    protected RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .addHeader("x-api-key", ConfigManager.getApiKey())
                .setContentType("application/json")
                .build();
    }
}