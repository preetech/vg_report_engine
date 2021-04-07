package au.com.vg.engine;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApplicationTests {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/reportengine/";
    }

    @Test
    public void testGetTradingReport() {
        String response = createGetRequest("api/public/generateReport")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        Assert.assertTrue(response.contains("Report file generated in path"));
        try {
            Files.delete(Paths.get(response.substring(response.indexOf(":") + 2)));
            log.info("Test File deleted Successfully");
        } catch (IOException e) {
            log.error("Exception happened while deleting the records: {}", e.getMessage());
        }
    }

    private Response createGetRequest(String url) {
        return given()
                .when()
                .get(url);
    }
}
