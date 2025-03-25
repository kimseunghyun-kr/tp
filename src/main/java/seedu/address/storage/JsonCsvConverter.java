package seedu.address.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



/**
 * Converts between JSON and CSV formats with proper handling of complex structures.
 */
public class JsonCsvConverter {
    private final ObjectMapper mapper;
    private final QueryValidator validator;

    /**
     * Creates a new JsonCsvConverter with the given ObjectMapper and QueryValidator.
     * @param mapper The ObjectMapper to use for JSON conversion.
     * @param validator The QueryValidator to use for validation.
     */
    public JsonCsvConverter(ObjectMapper mapper, QueryValidator validator) {
        this.mapper = mapper;
        this.validator = validator;
    }

    /**
     * Converts an object to CSV format.
     * @param obj The object to convert.
     * @return The CSV representation of the object.
     * @throws JsonProcessingException If the object cannot be converted.
     */
    public String toCsv(Object obj) throws JsonProcessingException {
        validator.validateForWrite(obj);

        // Convert the object to a JSON tree
        JsonNode rootNode = mapper.valueToTree(obj);
        StringBuilder csvBuilder = new StringBuilder();

        if (rootNode.has("persons") && rootNode.get("persons").isArray()) {
            // Process the persons array (special handling for AddressBook)
            ArrayNode persons = (ArrayNode) rootNode.get("persons");

            // Define CSV header
            List<String> headers = Arrays.asList(
                    "employeeId", "name", "phone", "email", "jobposition", "tags",
                    "anniversaryDate", "anniversaryType", "anniversaryName", "anniversaryDescription"
            );
            csvBuilder.append(String.join(",", headers)).append("\n");

            // Process each person
            for (JsonNode person : persons) {
                String employeeId = escapeForCsv(person.path("employeeId").asText());
                String name = escapeForCsv(person.path("name").asText());
                String phone = escapeForCsv(person.path("phone").asText());
                String email = escapeForCsv(person.path("email").asText());
                String jobPosition = escapeForCsv(person.path("jobposition").asText());

                // Process tags array
                JsonNode tags = person.path("tags");
                StringBuilder tagsStr = new StringBuilder();
                if (tags.isArray()) {
                    for (int i = 0; i < tags.size(); i++) {
                        if (i > 0) {
                            tagsStr.append(";");
                        }
                        tagsStr.append(tags.get(i).asText());
                    }
                }
                String tagsFormatted = escapeForCsv(tagsStr.toString());

                // Process anniversaries array and duplicate rows accordingly
                JsonNode anniversaries = person.path("anniversaries");

                if (anniversaries.isArray() && anniversaries.size() > 0) {
                    // If there are multiple anniversaries, create a separate row for each
                    for (JsonNode anniv : anniversaries) {
                        String annivDate = escapeForCsv(anniv.path("date").asText());
                        String annivType = escapeForCsv(anniv.path("type").path("name").asText());
                        String annivName = escapeForCsv(anniv.path("name").asText());
                        String annivDescription = escapeForCsv(anniv.path("description").asText());

                        csvBuilder.append(String.join(",",
                                        employeeId, name, phone, email, jobPosition, tagsFormatted,
                                        annivDate, annivType, annivName, annivDescription))
                                .append("\n");
                    }
                } else {
                    // No anniversaries: Insert a single row with empty anniversary fields
                    csvBuilder.append(String.join(",",
                                    employeeId, name, phone, email, jobPosition, tagsFormatted,
                                    "", "", "", ""))
                            .append("\n");
                }
            }
        }

        return csvBuilder.toString();
    }

    /**
     * Escapes values for CSV output.
     * Ensures proper formatting by handling commas, quotes, and newlines.
     */
    private String escapeForCsv(String value) {
        if (value == null || value.isEmpty()) {
            return "\"\"";
        }

        // Escape quotes by doubling them and wrap in quotes if necessary
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Converts CSV data to an object.
     * @param csvData The CSV data to convert.
     * @param clazz The class of the object to convert to.
     * @param <T> The type of the object to convert to.
     * @return The object represented by the CSV data.
     * @throws IOException If the CSV data cannot be converted.
     */
    public <T> T fromCsv(String csvData, Class<T> clazz) throws IOException {
        validator.validateForRead(csvData);

        // For JsonSerializableAddressBook, use special parser
        if (clazz.equals(JsonSerializableAddressBook.class)) {
            return parseAddressBookCsv(csvData, clazz);
        }

        throw new IOException("Unsupported conversion from CSV to " + clazz.getName());
    }

    private <T> T parseAddressBookCsv(String csvData, Class<T> clazz) throws IOException {
        String[] lines = csvData.split("\n");
        if (lines.length < 1) {
            throw new IOException("CSV data is empty");
        }

        // Parse header
        String[] headers = parseCsvLine(lines[0]);

        // Create the address book structure
        ObjectNode rootNode = mapper.createObjectNode();
        ArrayNode personsArray = rootNode.putArray("persons");

        // Process each data row
        for (int i = 1; i < lines.length; i++) {
            String[] values = parseCsvLine(lines[i]);
            if (values.length < headers.length) {
                continue;
            }

            ObjectNode personNode = mapper.createObjectNode();

            // Map basic fields
            Map<String, Integer> headerIndices = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                headerIndices.put(headers[j], j);
            }

            // Set basic properties
            personNode.put("employeeId", values[headerIndices.get("employeeId")]);
            personNode.put("name", values[headerIndices.get("name")]);
            personNode.put("phone", values[headerIndices.get("phone")]);
            personNode.put("email", values[headerIndices.get("email")]);
            personNode.put("jobposition", values[headerIndices.get("jobposition")]);

            // Process tags
            String tagsStr = values[headerIndices.get("tags")];
            ArrayNode tagsArray = personNode.putArray("tags");
            if (!tagsStr.isEmpty()) {
                for (String tag : tagsStr.split(";")) {
                    tagsArray.add(tag.trim());
                }
            }

            // Process anniversaries
            String annivsStr = values[headerIndices.get("anniversaries")];
            ArrayNode annivsArray = personNode.putArray("anniversaries");
            if (!annivsStr.isEmpty()) {
                for (String annivStr : annivsStr.split("\\|")) {
                    String[] parts = annivStr.split(":");
                    if (parts.length >= 4) {
                        ObjectNode annivNode = mapper.createObjectNode();
                        annivNode.put("date", parts[0]);
                        annivNode.put("type", parts[1]);
                        annivNode.put("name", parts[2]);
                        annivNode.put("description", parts[3]);
                        annivsArray.add(annivNode);
                    }
                }
            }

            personsArray.add(personNode);
        }

        return mapper.convertValue(rootNode, clazz);
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++;
                } else {
                    // Toggle quote mode
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                result.add(unescapeFromCsv(currentField.toString()));
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        result.add(unescapeFromCsv(currentField.toString()));
        return result.toArray(new String[0]);
    }

    /**
     * Unescapes a field from CSV format.
     * @param field The field to unescape.
     * @return The unescaped field.
     */
    private String unescapeFromCsv(String field) {
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }
        return field.replace("\"\"", "\"");
    }
}
