package cdit_automation.models.datasource;

import cdit_automation.enums.datasource.HomeTypeEnum;
import cdit_automation.enums.datasource.SpecialMappingEnum;
import cdit_automation.models.datasource.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "special_property")
public class SpecialProperty extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "property_entity_key")
    @NotNull
    private Property property;

    @Column(name = "main_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private SpecialMappingEnum mainType;

    @Column(name = "sub_type")
    @Enumerated(EnumType.STRING)
    private HomeTypeEnum subType;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;

    public static SpecialProperty create(Batch batch, Property property, SpecialMappingEnum specialMappingEnum, HomeTypeEnum homeTypeEnum, BiTemporalData biTemporalData) {
        return build(batch, property, specialMappingEnum, homeTypeEnum, biTemporalData);
    }

    private static SpecialProperty build(Batch batch, Property property, SpecialMappingEnum specialMappingEnum, HomeTypeEnum homeTypeEnum, BiTemporalData biTemporalData) {
        return SpecialProperty.builder().batch(batch).property(property).mainType(specialMappingEnum).subType(homeTypeEnum).biTemporalData(biTemporalData).build();
    }
}
