package neotech.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.IntegrationTest;

import java.io.IOException;
import java.util.Collections;

import static neotech.services.CountryDetector.CountryDetectorResponse;
import static org.junit.Assert.assertEquals;


@IntegrationTest
public class TreeCountryDetectorIT {

    private TreeCountryDetector treeCountryDetector;

    @Before
    public void setUp() throws Exception {
        this.treeCountryDetector = new TreeCountryDetector(new WikipediaCountryListSupplier());
    }

    @Test
    public void listCountries() throws IOException {
        assertEquals(new CountryDetectorResponse(Collections.singletonList("Latvia")), treeCountryDetector.getCountry("+371671123456"));
        assertEquals(new CountryDetectorResponse(Collections.singletonList("Russia")), treeCountryDetector.getCountry("+71671123456"));
        assertEquals(new CountryDetectorResponse(Collections.singletonList("Guam")), treeCountryDetector.getCountry("+1671123456"));
    }

}
