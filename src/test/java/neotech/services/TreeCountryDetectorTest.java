package neotech.services;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static neotech.services.CountryDetector.*;
import static neotech.services.CountryDetector.CountryDetectorResponse.*;
import static neotech.services.CountryListSupplier.STUPID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class TreeCountryDetectorTest {

    private static final String LATVIA       = "Latvia";
    private static final String SWITZERLAND  = "Switzerland";
    private static final String RUSSIA       = "Russia";
    private static final String CANADA       = "Canada";
    private static final String USA          = "United States";
    private static final String CARRIBEAN    = "Carribean";
    private static final String GUAM         = "Guam";

    private TreeCountryDetector treeCountryDetector;

    @Before
    public void setUp() throws Exception {
        treeCountryDetector = new TreeCountryDetector(STUPID);
    }

    @Test
    public void shouldDetectCountryByPhoneNumberForDifferentPrefixLengths() {
        assertEquals(new CountryDetectorResponse(Collections.singletonList(RUSSIA)),      treeCountryDetector.getCountry("+74952329760"));     // one digit prefix number
        assertEquals(new CountryDetectorResponse(Collections.singletonList(SWITZERLAND)), treeCountryDetector.getCountry("+41438176546"));     // two digits prefix number
        assertEquals(new CountryDetectorResponse(Collections.singletonList(LATVIA)),      treeCountryDetector.getCountry("+37167588675"));     // three digits prefix number
        assertThat(treeCountryDetector.getCountry("+12345678900").getNames(), containsInAnyOrder(CANADA, USA, CARRIBEAN));                     // three countries for same prefix
        assertEquals(new CountryDetectorResponse(Collections.singletonList(GUAM)),        treeCountryDetector.getCountry("+16715678900"));     // one country under another country
    }

    @Test
    public void shouldReturnNotFoundIfUnableToFindCountry() {
        assertEquals(NOT_FOUND, treeCountryDetector.getCountry("+99712345678"));  // unexisting country number
    }

    @Test
    public void shouldProperlyHandleInvalidPhoneNumber() {
        assertEquals(INVALID_PHONE_NUMBER, treeCountryDetector.getCountry("371"));
        assertEquals(INVALID_PHONE_NUMBER, treeCountryDetector.getCountry("+371asdfasdf"));
        assertEquals(INVALID_PHONE_NUMBER, treeCountryDetector.getCountry("+371"));
        assertEquals(INVALID_PHONE_NUMBER, treeCountryDetector.getCountry("37167588675"));
    }

    @Test
    public void shouldNotBeAbleToAddACountryWithIncorrectPrefix() {
        treeCountryDetector.addCountry("+370", "Lithuania");
        assertEquals(NOT_FOUND, treeCountryDetector.getCountry("+37052131230"));
    }

    @Test
    public void regexpTest() {
        assertFalse(treeCountryDetector.isValidPhoneNumber(""));
        assertFalse(treeCountryDetector.isValidPhoneNumber("+371asdfasdf"));
        assertFalse(treeCountryDetector.isValidPhoneNumber("371"));
        assertFalse(treeCountryDetector.isValidPhoneNumber("+371"));
        assertTrue(treeCountryDetector.isValidPhoneNumber("+37126357209"));
    }


}
