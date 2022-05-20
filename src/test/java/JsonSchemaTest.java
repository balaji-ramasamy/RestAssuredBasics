import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class JsonSchemaTest {

    @Test
    public void jsonSchemaTest() {
        given()
                .baseUri("https://postman-echo.com")
                .log().all()
                .when()
                .get("/get")
                .then().log().all()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("Echo-Get-JsonSchema.json"));

    }
}
