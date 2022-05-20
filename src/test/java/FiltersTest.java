import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class FiltersTest {

    @Test
    public void filters_Test() {
        given()
                .baseUri("https://postman-echo.com")
                .filter(new RequestLoggingFilter(LogDetail.BODY))
                .filter(new ResponseLoggingFilter(LogDetail.STATUS))
                .when()
                .get("/get")
                .then();

    }

    @Test
    public void filters_log_into_file_Test() {
        try(PrintStream printStream = new PrintStream(new File("restassured.log"))){
            given()
                    .baseUri("https://postman-echo.com")
                    .filter(new RequestLoggingFilter(LogDetail.BODY, printStream))
                    .filter(new ResponseLoggingFilter(LogDetail.STATUS, printStream))
                    .when()
                    .get("/get")
                    .then();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
