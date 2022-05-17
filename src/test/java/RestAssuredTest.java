import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");

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



}
