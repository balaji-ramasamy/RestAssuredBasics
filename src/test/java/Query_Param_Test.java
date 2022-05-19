import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Query_Param_Test {

    @Test
    public void singleQueryParamTest() {
        given()
                .baseUri("https://postman-echo.com")
                .queryParam("foo1","bar1")
                .when()
                .get("/get")
                .then().log().all();
    }

    @Test
    public void multipleQueryParamTest() {

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("foo1", "bar1");
        queryParams.put("foo2", "bar2");

        given()
                .baseUri("https://postman-echo.com")
                .queryParams(queryParams)
                .when()
                .get("/get")
                .then().log().all();
    }

    @Test
    public void multipleValueQueryParamTest() {
        given()
                .baseUri("https://postman-echo.com")
                .queryParam("foo1","bar1","bar2","bar3")
                .log().all()
                .when()
                .get("/get")
                .then().log().all();
    }
}
