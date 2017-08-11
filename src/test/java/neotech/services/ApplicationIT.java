package neotech.services;

import com.jayway.restassured.RestAssured;
import neotech.Application;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ApplicationIT extends FluentTest {

    @Value("${local.server.port}")
    private int serverPort;
    private WebDriver webDriver = new PhantomJSDriver();

    private String getUrl() {
        return "http://localhost:" + serverPort;
    }

    @Autowired
    private TreeCountryDetector countryDetector;

    @Before
    public void setUp() {
        countryDetector.addCountry("371", "Latvia");
    }

    @Test
    public void returnsCountryInCaseAValidNumberIsGiven() {
        goTo(getUrl());
        fill("#phoneNumber").with("+37126357209");
        find("#determine").click();
        await().atMost(5, TimeUnit.SECONDS).until("#console").hasText("Latvia");
        assertThat(find("#console").getTexts()).containsOnly("Latvia");
    }

    @Test
    public void returnsNotFoundInCaseAnInValidNumberIsGiven() {
        goTo(getUrl());
        fill("#phoneNumber").with("+99712345678");
        find("#determine").click();
        await().atMost(5, TimeUnit.SECONDS).until("#console").hasText("Not Found");
        assertThat(find("#console").getTexts()).containsOnly("Not Found");
    }

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

}
