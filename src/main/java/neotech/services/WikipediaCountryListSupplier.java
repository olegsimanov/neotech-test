package neotech.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("WikipediaCountryListSupplier")
public class WikipediaCountryListSupplier implements CountryListSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaCountryListSupplier.class);

    private List<CountryPrefixAndName> countryPrefixAndNameList;

    public WikipediaCountryListSupplier() throws Exception {
        countryPrefixAndNameList = cleanUp(loadCountriesFrom(new URL("https://en.wikipedia.org/wiki/List_of_country_calling_codes")));
    }

    @Override
    public List<CountryPrefixAndName> list() {
        return countryPrefixAndNameList;
    }

    private List<CountryPrefixAndName> cleanUp(List<CountryPrefixAndName> uncleaned) {
        List<CountryPrefixAndName> cleaned = new ArrayList<>();
        for (CountryPrefixAndName countryPrefixAndName : uncleaned) {
            String prefix = countryPrefixAndName.getPrefix().trim();
            if (prefix.startsWith("+")) {
                prefix = prefix.substring(1, prefix.length());
            }
            prefix = prefix.replace(" ", "");
            if (!Character.isDigit(prefix.charAt(prefix.length() - 1))) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
            String[] prefixes = prefix.split("/");
            for (String p : prefixes) {
                cleaned.add(new CountryPrefixAndName(p, countryPrefixAndName.getName()));
            }
        }
        return cleaned;
    }

    private List<CountryPrefixAndName> loadCountriesFrom(URL url) throws IOException {

        List<CountryPrefixAndName> countryPrefixAndNames = new ArrayList<>();

        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_country_calling_codes").get();
        Elements elements = doc.select("ul");
        elements.forEach(element -> {
            String elementAsString = element.toString();
            String pattern = "(\\+[0-9]{1,} ?[0-9]{0,}</a>)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(elementAsString);
            if (m.find()) {
                String[] lines = elementAsString.split("\n");
                for (String line : lines) {
//                    System.out.println(line);

                    boolean numberFound = false;
                    boolean skipToNewLine = false;
                    boolean insideNumber = false;
                    boolean insideLabel = false;
                    String tmpPrefix = null;
                    StringBuilder tmp = new StringBuilder();
                    for (int i = 0; i < line.length() - 1; i++) {
                        if (line.toLowerCase().contains("discontinued") || line.contains("unassigned") || line.contains("formerly")) {
                            continue;
                        }

                        char curChar = line.charAt(i);
                        if (insideNumber) {
                            if (curChar == '<') {
                                insideNumber = false;
                                numberFound = true;
                                tmpPrefix = tmp.toString();
                                tmp = new StringBuilder();
                            } else {
                                tmp.append(curChar);
                            }
                        } else if (insideLabel) {
                            if (curChar == '\"') {
//                                System.out.println(tmpPrefix + " -> " + tmp.toString());
                                countryPrefixAndNames.add(new CountryPrefixAndName(tmpPrefix, tmp.toString()));
                                tmp = new StringBuilder();
                                tmpPrefix = null;
                                insideLabel = false;
                                skipToNewLine = true;
                            } else {
                                tmp.append(curChar);
                            }
                        } else {

                            if (curChar == '\n') {
                                numberFound = false;
                                skipToNewLine = false;
                                insideNumber = false;
                                insideLabel = false;
                                tmpPrefix = null;
                                tmp = new StringBuilder();
                            }

                            if (!skipToNewLine) {
                                if (curChar == '>' && line.charAt(i + 1) == '+') {
                                    insideNumber = true;
                                } else if (numberFound && curChar == 't' && line.substring(i, i + 7).equals("title=\"")) {
                                    insideLabel = true;
                                    i += 6;
                                }
                            }
                        }
                    }
                }
            }
        });

        return countryPrefixAndNames;
    }

}
