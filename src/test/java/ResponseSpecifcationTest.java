import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ResponseSpecifcationTest {

    ResponseSpecification responseSpecification;
    RequestSpecification requestSpecificationUsingBuilder;
    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey ="PMAK-627c5ad78bbfca15225264ca-b5a81b2feedf37166c56809f39cddbecbe";

    @BeforeClass
    public void before(){
        RequestSpecBuilder requestSpecificationBuilder = new RequestSpecBuilder().setBaseUri(baseURL)
                .addHeader("x-api-key",apiKey).log(LogDetail.ALL);
        requestSpecificationUsingBuilder = requestSpecificationBuilder.build();
        RestAssured.requestSpecification = requestSpecificationBuilder.build();

        //method 1:
        responseSpecification = RestAssured.expect()
                                                .statusCode(200)
                                                .contentType(ContentType.JSON)
                                                .logDetail(LogDetail.ALL);

        // method 2: using ResponseSpecbuilder

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();

        //using default responseSpecification
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void basicTest() {
        Response response = get("/workspaces").then().spec(responseSpecification).extract().response();
        assertThat(response.path("workspaces[0].name").toString(), equalTo("My Workspace"));

    }

    @Test
    public void using_ResponseSpecbuilder_Test() {
        Response r = get("/workspaces").then().spec(responseSpecification).extract().response();
        assertThat(r.path("workspaces[0].name").toString(), equalTo("My Workspace"));
    }

    @Test
    public void using_Default_ResponseSpecification_Test() {
        Response r = get("/workspaces").then().extract().response();
        assertThat(r.path("workspaces[0].name").toString(), equalTo("My Workspace"));

    }
}
