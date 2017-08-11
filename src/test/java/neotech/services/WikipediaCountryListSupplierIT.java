package neotech.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.IntegrationTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@IntegrationTest
public class WikipediaCountryListSupplierIT {

    private CountryListSupplier countryListSupplier;

    @Before
    public void setUp() throws Exception {
        this.countryListSupplier = new WikipediaCountryListSupplier();
    }

    @Test
    public void listCountries() throws IOException {
        assertEquals(310, countryListSupplier.list().size());
//        countryListSupplier.list().forEach(System.out::println);
    }

}
