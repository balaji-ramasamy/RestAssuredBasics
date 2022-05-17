import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class RestAssuredTest {

    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey ="PMAK-627c5ad78bbfca15225264ca-b5a81b2feedf37166c56809f39cddbecbe";

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



}
