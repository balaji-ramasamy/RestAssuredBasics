import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RequestSpecificationTest {
    RequestSpecification requestSpecification;
    RequestSpecification requestSpecificationUsingBuilder;
    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey ="PMAK-627c5ad78bbfca15225264ca-b5a81b2feedf37166c56809f39cddbecbe";

    @BeforeClass
   public void before(){
   //method1:
        requestSpecification = with().baseUri(baseURL)
            .header("x-api-key",apiKey);


   //method2: Using Request Spec Builder
        RequestSpecBuilder requestSpecificationBuilder = new RequestSpecBuilder().setBaseUri(baseURL)
                .addHeader("x-api-key",apiKey).log(LogDetail.ALL);
        requestSpecificationUsingBuilder = requestSpecificationBuilder.build();

        //using default request specification
        RestAssured.requestSpecification = requestSpecificationBuilder.build();
   }

    @Test
    public void basicTest() {

        given()
                .spec(requestSpecification)
        .when()
                .get("/workspaces")
        .then()
                .assertThat()
                .statusCode(200);
    }


    //BDD to Non-BDD
    @Test
    public void validate_status_code_Test() {
       Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
       assertThat(response.statusCode(),is(equalTo(200)));
       assertThat(response.path("workspaces[0].name").toString(), equalTo("My Workspace"));
    }

    //using RequestSpecificationBuilder to build request Specification
    @Test
    public void using_RequestSpecificationBuilder_Test() {

       Response resp =  given(requestSpecificationUsingBuilder)
                .get("/workspaces")
                .then()
                .extract()
                .response();
        assertThat(resp.statusCode(),is(equalTo(200)));

    }

    //using default request specification
    @Test
    public void using_Default_requestSpecification_Test() {

        Response response = get("/workspaces").then().extract().response();
        assertThat(response.statusCode(),is(equalTo(200)));
    }

    //query any RequestSpecification
    @Test
    public void query_Any_ReqSpec_Test() {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(RestAssured.requestSpecification);
        System.out.println(queryableRequestSpecification.getBaseUri());

    }


}
