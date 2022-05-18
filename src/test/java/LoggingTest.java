import io.restassured.config.LogConfig;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class LoggingTest {
    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");

    @Test
    public void reqAndRespLoggingTest() {
        given().
                baseUri(baseURL)
                .header("x-api-key", apiKey)
                .log().all()
                .when()
                .get("/workspaces")
                .then()
                .log().body();

    }

    @Test
    public void logIfErrorTest() {
        given().
                baseUri(baseURL)
                .header("x-api-key",apiKey)
                .when()
                .get("/workspaces")
                .then()
                .log()
                .ifError()
                .assertThat().
                statusCode(200);

    }

    @Test
    public void logIfValidationFailsTest() {
        given()
                .baseUri(baseURL)
                .header("x-api-key", apiKey)
                .config(config().logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .when()
                .get("/workspaces")
                .then()
                .assertThat().statusCode(201);

    }
}
