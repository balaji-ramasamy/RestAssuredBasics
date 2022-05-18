import io.restassured.config.LogConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class HeaderTest {
    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");
    private static final String baseURL2 = "https://807f7989-a914-491c-8266-47e24668cbe8.mock.pstmn.io";
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
