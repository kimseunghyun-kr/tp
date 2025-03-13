package seedu.address.model.anniversary;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;



/**
 * class denoting an actual anniversary
 */
@Data
@AllArgsConstructor
public class Anniversary {
    private final UUID uuid;
    private final LocalDate date;
    private final Set<AnniversaryType> type;
    private final String description;
    private final String name;
}
