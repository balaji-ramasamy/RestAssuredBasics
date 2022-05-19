import io.restassured.config.EncoderConfig;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class Multipart_Form_Data_Test {
    @Test
    public void basic_Test() {
        given()
                .baseUri("https://postman-echo.com")
                .multiPart("foo1","bar1")
                .log().all()
                .when()
                .post("/post")
                .then().log().all();

    }

    @Test
    public void upload_File_Test() {
        String attributes = "{\"name\":\"upload.txt\",\"parent\":{\"id\":\"123456\"}}";
        given()
                .baseUri("https://postman-echo.com")
                .multiPart("file", new File("src/test/resources/upload.txt"))
                .multiPart("attributes",attributes,"application/json")
                .log().all()
                .when()
                .post("/post")
                .then()
                .log().all();
    }

    @Test
    public void download_File_Test() {
        byte[] bytes = given().
                                baseUri("https://github.com")
                                .log().all().
                        when().
                                post("/appium/appium/blob/master/sample-code/apps/ApiDemos-debug.apk")
                        .then()
                                .log().all()
                                .extract()
                                .response().asByteArray();

        try(OutputStream os = new FileOutputStream("ApiDemos-debug.apk")){
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void form_URL_encoded_Test() {
        given()
                .baseUri("https://postman-echo.com")
                .formParams("key1","value1")
                .formParams("key 2","value 2")
                .config(config().encoderConfig(
                        EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .log().all()
                .when()
                .post("/post")
                .then().log().all();

    }
}
