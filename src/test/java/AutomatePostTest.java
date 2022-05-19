import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomatePostTest {

    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");


    @BeforeClass
    public void before(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(baseURL)
                .addHeader("x-api-key", "PMAK-627c5ad78bbfca15225264ca-b5a81b2feedf37166c56809f39cddbecbe")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.responseSpecification = responseSpecBuilder.build();
    }


    // BDD style
    @Test
    public void createWorkspaceTest() {
        File payload = new File("src/test/resources/CreateWorkspacePayload.json");

         given()
                 .body(payload)
        .when()
                .post("/workspaces")
        .then()
                 .assertThat()
                 .body("workspace.name",equalTo("mySecondWorkspace"),
                         "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));

    }

    // Non BDD style
    @Test
    public void createWorkspaceTest_Non_BDD_Style() {
        HashMap<String, Object> mainObject = new HashMap<>();
        HashMap<String, Object> nestedObject = new HashMap<>();
        nestedObject.put("name", "myThirdWorkspace");
        nestedObject.put("type","personal");
        nestedObject.put("description","created by RestAssured");

        mainObject.put("workspace", nestedObject);

        Response response = with()
                .body(mainObject)
                .post("/workspaces");

                assertThat(response.path("workspace.name").toString(), equalTo("myThirdWorkspace"));
                assertThat(response.path("workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));
    }


}
