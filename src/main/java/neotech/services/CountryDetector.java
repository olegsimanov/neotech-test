package neotech.services;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public interface CountryDetector {

    final class CountryDetectorResponse {

        public static final CountryDetectorResponse INVALID_PHONE_NUMBER    = new CountryDetectorResponse("Invalid phone number. Should be like: +1234567890");
        public static final CountryDetectorResponse NOT_FOUND               = new CountryDetectorResponse("Not Found");

        private String errorMessage;
        private List<String> names = Collections.emptyList();

        public CountryDetectorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public CountryDetectorResponse(List<String> names) {
            this.names = names;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CountryDetectorResponse that = (CountryDetectorResponse) o;

            if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null)
                return false;
            return names != null ? names.equals(that.names) : that.names == null;
        }

        @Override
        public int hashCode() {
            int result = errorMessage != null ? errorMessage.hashCode() : 0;
            result = 31 * result + (names != null ? names.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "CountryDetectorResponse{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", names=" + names +
                    '}';
        }
    }

    CountryDetectorResponse getCountry(String phoneNumber);

}
