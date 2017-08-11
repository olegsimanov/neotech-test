package neotech.services;

import java.util.Arrays;
import java.util.List;

public interface CountryListSupplier {

    CountryListSupplier STUPID = new CountryListSupplier() {
        @Override
        public List<CountryPrefixAndName> list() {
            return Arrays.asList(
                    new CountryPrefixAndName("371", "Latvia"),
                    new CountryPrefixAndName("41", "Switzerland"),
                    new CountryPrefixAndName("7", "Russia"),
                    new CountryPrefixAndName("1", "Canada"),
                    new CountryPrefixAndName("1", "United States"),
                    new CountryPrefixAndName("1", "Carribean"),
                    new CountryPrefixAndName("1671", "Guam")
            );

        }
    };


    final class CountryPrefixAndName {

        private String prefix;
        private String name;

        public CountryPrefixAndName() {
        }

        public CountryPrefixAndName(String prefix, String name) {
            this.prefix = prefix;
            this.name = name;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CountryPrefixAndName that = (CountryPrefixAndName) o;

            if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null) return false;
            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            int result = prefix != null ? prefix.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "CountryPrefixAndName{" +
                    "prefix='" + prefix + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    List<CountryPrefixAndName> list();

}
