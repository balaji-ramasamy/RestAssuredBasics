import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class JacksonAPI_test {

    private static final String baseURL = "https://api.getpostman.com";
    private static final String apiKey = System.getProperty("postmanKey");


    @BeforeClass
    public void before() {
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
    public void jackson_Api_Test() {
        HashMap<String, Object> mainObject = new HashMap<>();
        HashMap<String, Object> nestedObject = new HashMap<>();
        nestedObject.put("name", "myFourthWorkspace");
        nestedObject.put("type", "personal");
        nestedObject.put("description", "created by RestAssured");

        mainObject.put("workspace", nestedObject);

        Response response = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String mainobjStr = mapper.writeValueAsString(mainObject);
            response = with()
                    .body(mainobjStr)
                    .post("/workspaces");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(response)) {
            assertThat(response.path("workspace.name").toString(), equalTo("myFourthWorkspace"));
            assertThat(response.path("workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));
        }
    }

    @Test
    public void jackson_Api_Object_Node_Test() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nestedObjNode = mapper.createObjectNode();
        ObjectNode mainObjNode = mapper.createObjectNode();

        nestedObjNode.put("name", "myFifthWorkspace");
        nestedObjNode.put("type", "personal");
        nestedObjNode.put("description", "created by RestAssured");

        mainObjNode.set("workspace", nestedObjNode);

        Response response = null;

        try {
            String mainobjStr = mapper.writeValueAsString(mainObjNode);
            response = with()
                    .body(mainobjStr)
                    .post("/workspaces");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(response)) {
            assertThat(response.path("workspace.name").toString(), equalTo("myFifthWorkspace"));
            assertThat(response.path("workspace.id").toString(), matchesPattern("^[a-z0-9-]{36}$"));
        }
    }



}
