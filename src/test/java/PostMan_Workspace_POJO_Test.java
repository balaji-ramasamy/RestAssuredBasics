import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.pojo.workspace.Workspace;
import com.rest.pojo.workspace.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class PostMan_Workspace_POJO_Test {
    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey =System.getProperty("postmanKey");


    @BeforeClass
    public void before(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(baseURL)
                .addHeader("x-api-key", "PMAK-627c5ad78bbfca15225264ca-b5a81b2feedf37166c56809f39cddbecbe")
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL));

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test(dataProvider = "getData")
    public void Workspace_POJO_Test(String wsName, String type, String description) {

        Workspace workspace = new Workspace(wsName,type,description);
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);


        ObjectMapper mapper = new ObjectMapper();
        WorkspaceRoot deserializedWorkspaceRoot = with()
                .body(workspaceRoot)
                .post("/workspaces")
                .then()
                .extract()
                .as(WorkspaceRoot.class);

        assertThat( deserializedWorkspaceRoot.getWorkspace().getName(), equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat( deserializedWorkspaceRoot.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));
    }


    @DataProvider
    public Object[][] getData(){
        return new Object[][]{
                {"my8thws", "personal","created by RA"},
                {"my9thws","team","decription"}
        };
    }
}
