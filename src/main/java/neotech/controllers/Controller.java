package neotech.controllers;

import neotech.services.CountryDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/country")
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public static final class PhoneNumber {

        private String number;

        public PhoneNumber() {
        }

        public PhoneNumber(String number) {
            this.number = number;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PhoneNumber that = (PhoneNumber) o;

            return number != null ? number.equals(that.number) : that.number == null;
        }

        @Override
        public int hashCode() {
            return number != null ? number.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "PhoneNumber{" +
                    "number='" + number + '\'' +
                    '}';
        }
    }

    private CountryDetector countryDetector;

    @Autowired
    public Controller(@Qualifier("TreeCountryDetector") CountryDetector countryDetector) {
        this.countryDetector = countryDetector;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    CountryDetector.CountryDetectorResponse findCountry(@RequestBody PhoneNumber phoneNumber) {
        CountryDetector.CountryDetectorResponse country = countryDetector.getCountry(phoneNumber.getNumber());
        LOGGER.debug("Country for phoneNumber: {} found: {}", phoneNumber.getNumber(), country);
        return country;
    }

}