import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomatePutTest {


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

    @Test
    public void updateWorkspace_BDD_Style_Test() {

        String workspaceId="d560a05c-4830-4cf4-b5a9-7ad7f265407e";

        String payload="{\n" +
                "    \"workspace\":{\n" +
                "        \"name\": \"newWorkspaceName1\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\":\"updated by RestAssured.\"\n" +
                "    }\n" +
                "}";

        given()
                .body(payload)
                .pathParam("workspaceId",workspaceId)
        .when()
                .put("/workspaces/{workspaceId}")
        .then()
                .assertThat().body("workspace.name",equalTo("newWorkspaceName1"),
                        "workspace.id",matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", equalTo(workspaceId));


    }


    @Test
    public void updateWorkspaceTest() {
        String workspaceId="d560a05c-4830-4cf4-b5a9-7ad7f265407e";
        String payload="{\n" +
                "    \"workspace\":{\n" +
                "        \"name\": \"myFirstWorkspace3\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\":\"updated by RestAssured.\"\n" +
                "    }\n" +
                "}";

        Response response=  with()
                .pathParam("workspaceId",workspaceId)
                .body(payload)
                .post("/workspaces/{workspaceId}");

        assertThat(response.path("workspace.name").toString(),equalTo("myFirstWorkspace3"));
        assertThat(response.path("workspace.id").toString(),equalTo(workspaceId));

    }

}
