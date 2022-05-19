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

public class AutomateDeleteTest {

    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");

    @BeforeClass
    public void before(){
       RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().setBaseUri(baseURL)
               .addHeader("x-api-key", "PMAK-627c5ad78bbfca15225264ca-b5a81b2feedf37166c56809f39cddbecbe")
               .log(LogDetail.ALL);
        RestAssured.requestSpecification= requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
                .expectStatusCode(200).log(LogDetail.ALL);
        RestAssured.responseSpecification=responseSpecBuilder.build();
    }

    @Test
    public void delete_workspace_BDD_Style_Test() {
        String workspaceId="991a9d38-8ef7-4e85-9901-21026c0f5b7f";
        given()
                .pathParam("workspaceId",workspaceId)
        .when()
                .delete("/workspaces/{workspaceId}")
        .then()
                .assertThat()
                .body("workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id",equalTo(workspaceId));

    }

    @Test
    public void delete_workspace_Non_BDD_Style_Test() {
        String workspaceId="991a9d38-8ef7-4e85-9901-21026c0f5b7f";
        Response response =with()
                .pathParam("workspaceId",workspaceId)
                .delete("/workspaces/{workspaceId}");

        assertThat(response.path("workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));
        assertThat(response.path("workspace.id").toString(), equalTo(workspaceId));

    }
}
