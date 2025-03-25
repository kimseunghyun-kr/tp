package seedu.address.storage;

/**
 * Validates the input data for conversion.
 */
public class QueryValidator {
    /**
     * Validates the object to be converted.
     * @param obj The object to validate.
     */
    public void validateForWrite(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to convert cannot be null.");
        }
    }
    /**
     * Validates the CSV data to be converted.
     * @param csvData The CSV data to validate.
     */
    public void validateForRead(String csvData) {
        if (csvData == null || csvData.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV data cannot be empty.");
        }
    }
}
