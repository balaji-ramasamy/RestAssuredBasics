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
    



}
