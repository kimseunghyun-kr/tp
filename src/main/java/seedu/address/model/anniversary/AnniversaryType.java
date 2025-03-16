package seedu.address.model.anniversary;

import lombok.Data;

/**
 * anniversary type, supposed to behave like a tag
 */
@Data
public class AnniversaryType {
    private final String name;
    private final String description;
}
