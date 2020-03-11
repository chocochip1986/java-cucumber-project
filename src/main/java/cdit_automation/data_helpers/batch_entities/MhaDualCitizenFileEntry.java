package cdit_automation.data_helpers.batch_entities;

import cdit_automation.utilities.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class MhaDualCitizenFileEntry {
    private String identiifer;

    public MhaDualCitizenFileEntry(String identifier) {
        this.identiifer = identifier;
    }

    public String toString() {
        return StringUtils.leftPad(identiifer, 9);
    }
}
