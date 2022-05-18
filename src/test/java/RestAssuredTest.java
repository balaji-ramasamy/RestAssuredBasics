import io.restassured.config.LogConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");
    private static final String baseURL2 = "https://807f7989-a914-491c-8266-47e24668cbe8.mock.pstmn.io";

    @Test
    public void assertStatusCodeTest(){
        given().
                baseUri(baseURL).
                header("x-api-key",apiKey).
                log().all().
        when().
                get("/workspaces").
        then().
                log().all()
                .assertThat()
                .statusCode(200);
    }
    
    @Test
    public void assertResponseBodyTest(){
        given().
                baseUri(baseURL).
                header("x-api-key",apiKey).
                log().all().
        when().
                get("/workspaces").
        then().
                log().all()
                .assertThat()
                .statusCode(200)
                .body("workspaces.name", hasItems("My Workspace", "New workspace 1", "New Workspace 2"));
    }

    @Test
    public void assertExtractResponseTest() {

        Response response = given().
                baseUri(baseURL).
                header("x-api-key",apiKey).
                log().all().
        when().
                get("/workspaces").
        then().
                log().all()
                .extract()
                .response();

        System.out.println(response.asString());

    }

    @Test
    public void extractSingleFieldTest() {

       String response = given().
                baseUri(baseURL)
                .header("x-api-key", apiKey)
                .log().all().
        when()
                .get("/workspaces").
                then().log().all()
                .extract().response().asString();

       //method1:
        //System.out.println((String) response.path("workspaces[0].name"));

        //method2:
        JsonPath jsonPath = new JsonPath(response);
        System.out.println(jsonPath.getString("workspaces[0].name"));


        //Method3:
        System.out.println(JsonPath.from(response).getString("workspaces[0].name"));

    }

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


    @Test
    public void blackListHeaderTest() {

        given()
                .baseUri(baseURL)
                .header("x-api-key",apiKey)
                .config(config().logConfig(LogConfig.logConfig().blacklistHeader("x-api-key")))
                .log().headers()
                .when()
                .get("/workspaces")
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void multipleHeaderTest() {

        //method2:
        Header header1 = new Header("header","value2");
        Header header2 = new Header("x-mock-match-request-headers","header");
        //method3
        Headers headers = new Headers(header1,header2);

        given().baseUri(baseURL2)
                //method1
                //.header("header","value2")
                //.header("x-mock-match-request-headers","header")
                //method2
                .header(header1)
                .header(header2)
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void multipleHeaderUsingHeadersTest() {
        Header header1 = new Header("header","value2");
        Header header2 = new Header("x-mock-match-request-headers","header");
        Headers headers = new Headers(header1,header2);

        given().baseUri(baseURL2)
                .headers(headers)
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void multipleHeaderUsingMapTest() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("header","value2");
        headerMap.put("x-mock-match-request-headers","header");
        given().baseUri(baseURL2)
                .headers(headerMap)
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void multiValueHeaderTest() {

        Header header1 = new Header("multiValueHeader","value1");
        Header header2 = new Header("multiValueHeader","value2");
        Header header3 = new Header("x-mock-match-request-headers","header");
        Headers headers = new Headers(header1,header2,header3);

        given().baseUri(baseURL2)
                //method1
                //.header("multiValueHeader","value1","value2")
                .headers(headers)
                .log().headers()
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void assertResponseHeaderTest() {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("header","value1");
        headerMap.put("x-mock-match-request-headers","header");
        given().baseUri(baseURL2)
                .headers(headerMap)
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("responseHeader", "value1");
    }

    @Test
    public void extract_Response_Header_Test() {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("header","value1");
        headerMap.put("x-mock-match-request-headers","header");
        Headers responseHeaders = given().baseUri(baseURL2)
                .headers(headerMap)
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .headers();

        System.out.println(responseHeaders.get("responseHeader").getName()+" : "+responseHeaders.get("responseHeader").getValue());
        System.out.println(responseHeaders.getValue("responseHeader"));
    }

    @Test
    public void extract_multivalue_response_header_Test() {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("header","value1");
        headerMap.put("x-mock-match-request-headers","header");
        Headers responseHeaders = given().baseUri(baseURL2)
                .headers(headerMap)
                .when()
                .get("/get")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .headers();

        List<String> values = responseHeaders.getValues("multiValueHeader");
        System.out.println(values);
    }
}
