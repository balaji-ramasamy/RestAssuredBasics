import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class ResponseAssertionTest {

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





}
