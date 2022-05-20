import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.pojo.simple.SimplePOJO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class Jackson_API_POJO_Test {

    @BeforeClass
    public void before() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri("https://807f7989-a914-491c-8266-47e24668cbe8.mock.pstmn.io")
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL));

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON);

        RestAssured.requestSpecification = requestSpecBuilder.build();
        RestAssured.responseSpecification = responseSpecBuilder.build();

    }

    @Test
    public void jackson_Api_Simple_Pojo_Serialization_Test() throws JsonProcessingException {
        SimplePOJO obj1 = new SimplePOJO("value1","value2");
        with()
                .body(obj1)
                .post("/postPojo")
                .then()
                .assertThat().body("key1",equalTo(obj1.getKey1()),
                        "key2",equalTo(obj1.getKey2()));

    }

    @Test
    public void jackson_Api_Simple_Pojo_Deserialization_Test() throws JsonProcessingException {
        SimplePOJO obj1 = new SimplePOJO("value1","value2");

        SimplePOJO deserializedPojo =  with()
                .body(obj1)
                .post("/postPojo")
                .then()
                .extract()
                .response()
                .as(SimplePOJO.class);

        ObjectMapper mapper = new ObjectMapper();
        String deserializedPojoStr = mapper.writeValueAsString(deserializedPojo);
        String simplePojoStr = mapper.writeValueAsString(obj1);
        assertThat(mapper.readTree(deserializedPojoStr), equalTo(mapper.readTree(simplePojoStr)));

    }
}
