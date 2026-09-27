package base;

import config.ConfigManager;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Listeners;

/**
 * Base class for all API test classes.
 * @Listeners registers Allure's TestNG listener so every test result
 * gets recorded for the Allure report.
 */
@Listeners(AllureTestNg.class)
public class BaseTest {

    protected RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .addHeader("x-api-key", ConfigManager.getApiKey())
                .setContentType("application/json")
                .build();
    }
}