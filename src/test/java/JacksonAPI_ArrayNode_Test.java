import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class JacksonAPI_ArrayNode_Test {
    @Test
    public void jackson_Api_Array_Node_Test() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        ObjectNode childObjNode1 = mapper.createObjectNode();
        ObjectNode childObjNode2 = mapper.createObjectNode();

        childObjNode1.put("id", "1234");
        childObjNode1.put("name", "abcd");

        childObjNode2.put("id", "5678");
        childObjNode2.put("name", "efgh");

        arrayNode.add(childObjNode1);
        arrayNode.add(childObjNode2);

        Response response = null;

        try {
            String mainobjStr = mapper.writeValueAsString(arrayNode);
            response = given()
                    .baseUri("https://807f7989-a914-491c-8266-47e24668cbe8.mock.pstmn.io")
                    .body(mainobjStr)
                    .post("/post");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(response)) {
            assertThat(response.path("msg").toString(), equalTo("Success."));
        }
    }
}
