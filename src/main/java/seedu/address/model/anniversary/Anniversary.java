package seedu.address.model.anniversary;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;



/**
 * class denoting an actual anniversary
 */
@Data
public class Anniversary {
    private final LocalDate date;
    private final Set<AnniversaryType> type;
    private final String description;
}
