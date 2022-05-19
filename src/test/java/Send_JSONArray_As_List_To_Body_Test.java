import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class Send_JSONArray_As_List_To_Body_Test {

    @BeforeClass
    public void before(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://807f7989-a914-491c-8266-47e24668cbe8.mock.pstmn.io/")
                .addHeader("x-mock-match-request-body", "true")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    // Non BDD style
    @Test
    public void createWorkspaceTest_Non_BDD_Style_JSONArray_as_list() {
        HashMap<String, String> object1 = new HashMap<>();
        HashMap<String, String> object2 = new HashMap<>();
        object1.put("id", "1234");
        object1.put("name","abcd");

        object2.put("id","5678");
        object2.put("name","efgh");

        List<Map<String, String>> list = new ArrayList<>();
        list.add(object1);
        list.add(object2);

        Response response = with()
                .body(list)
                .post("/post");

        assertThat(response.path("msg").toString(), equalTo("Success."));
    }
}
