import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Path_Param_Test {
    @Test
    public void Path_Parameter_Test() {
            given().
                    baseUri("https://reqres.in/")
                    .pathParam("userId","2")
                    .log().all()
                    .when()
                    .get("/api/users/{userId}")
                    .then().log().all();

    }

}
