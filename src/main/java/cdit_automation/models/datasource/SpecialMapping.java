package cdit_automation.models.datasource;

import cdit_automation.enums.datasource.SpecialMappingEnum;
import cdit_automation.models.datasource.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "special_mapping")
public class SpecialMapping extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SpecialMappingEnum type;

    @NotNull
    @Column(name = "value")
    private String value;

    @Nullable
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;

    public static SpecialMapping createLorongBuangkok(String postalCode, BiTemporalData biTemporalData) {
        return createLorongBuangkok(postalCode, null, biTemporalData);
    }

    public static SpecialMapping createLorongBuangkok(String postalCode, String description, BiTemporalData biTemporalData) {
        return build(SpecialMappingEnum.LORONG_BUANGKOK, postalCode, description, biTemporalData);
    }

    private static SpecialMapping build(SpecialMappingEnum specialMappingEnum, String value, String description, BiTemporalData biTemporalData) {
        return SpecialMapping.builder()
                .type(specialMappingEnum)
                .value(value)
                .description(description)
                .biTemporalData(biTemporalData)
                .build();
    }
}
