package neotech.services;

import com.jayway.restassured.RestAssured;
import neotech.Application;
import neotech.services.CountryDetector;
import neotech.services.TreeCountryDetector;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ControllerIT {

    @Value("${local.server.port}")
    private int serverPort;

    @Autowired
    private TreeCountryDetector countryDetector;

    @Before
    public void setUp() {
        countryDetector.addCountry("371", "Latvia");
        RestAssured.port = serverPort;
    }

    @Test
    public void happyPath() {
        given()
        .contentType("application/json")
        .body("{\"number\":\"+37167588675\"}")
        .when()
        .post("/country")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("names", hasItem("Latvia"));
    }

    @Test
    public void notFound() {
        given()
        .contentType("application/json")
        .body("{\"number\":\"+99712345678\"}")
        .when()
        .post("/country")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("errorMessage", equalTo("Not Found"));
    }

}
