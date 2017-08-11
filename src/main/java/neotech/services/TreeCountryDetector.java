package neotech.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static neotech.services.CountryDetector.CountryDetectorResponse.INVALID_PHONE_NUMBER;
import static neotech.services.CountryDetector.CountryDetectorResponse.NOT_FOUND;

@Component("TreeCountryDetector")
public class TreeCountryDetector implements CountryDetector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeCountryDetector.class);

    private class Node {

        private Set<String> data = new HashSet<>();
        private Map<String, Node> children = new HashMap<>();

        void add(String lastDigits, String identifier) {
            if (lastDigits.length() == 1) {
                Node child = children.get(lastDigits);
                if (child == null) {
                    child = new Node();
                    children.put(lastDigits, child);
                }
                child.addIdentifier(identifier);
            } else {
                String firstDigit = lastDigits.substring(0,1);
                Node child = children.get(firstDigit);
                if (child == null) {
                    child = new Node();
                    children.put(firstDigit, child);
                }
                String rest = lastDigits.substring(1, lastDigits.length());
                child.add(rest, identifier);
            }
        }

        private void addIdentifier(String identifier) {
            this.data.add(identifier);
        }

        List<String> getIdentifier(String lastDigits) {
            String firstDigit = lastDigits.substring(0,1);
            Node child = children.get(firstDigit);
            if (child == null) {
                return new ArrayList<>(this.data);
            } else {
                String rest = lastDigits.substring(1, lastDigits.length() - 1);
                List<String> childsIdentifiers = child.getIdentifier(rest);
                if (childsIdentifiers.isEmpty()) {
                    return new ArrayList<>(this.data);
                } else {
                    return childsIdentifiers;
                }
            }
        }


    }

    private Node root = new Node();

    @Autowired
    public TreeCountryDetector(@Qualifier("WikipediaCountryListSupplier") CountryListSupplier countryListSupplier) {
        countryListSupplier.list().forEach(el -> {
            LOGGER.debug("Adding: " + el.getPrefix() + ", " + el.getName());
            root.add(el.getPrefix(), el.getName());
        });
    }

    void addCountry(String code, String countryIdentifier) {
        if (!code.trim().isEmpty() && !code.trim().startsWith("+")) {
            root.add(code, countryIdentifier);
        }
    }

    @Override
    public CountryDetectorResponse getCountry(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber.trim())) {
            return INVALID_PHONE_NUMBER;
        }
        List<String> countryIdentifier = root.getIdentifier(phoneNumber.substring(1, phoneNumber.length()));
        if (countryIdentifier.isEmpty()) {
            return NOT_FOUND;
        } else {
            return new CountryDetectorResponse(countryIdentifier);
        }
    }

    boolean isValidPhoneNumber(String phoneNumber) {
        String pattern = "^\\+[0-9]{10,}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phoneNumber);
        return m.find();
    }

}
