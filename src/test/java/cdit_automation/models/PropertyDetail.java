package cdit_automation.models;

import cdit_automation.enums.FormatType;
import cdit_automation.enums.PropertyType;
import cdit_automation.models.AbstractEntity;
import cdit_automation.models.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "property_detail")
@SuperBuilder
@Check(constraints = "format_type IN ('MHA', 'NCA')")
public class PropertyDetail extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "floor")
    private String floor;

    @Column(name = "unit")
    private String unit;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_code")
    private String streetCode;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "new_postal_code")
    private String newPostalCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PropertyType propertyType;

    // TODO: To confirm nullable or NonNull requirements in future.
    @Enumerated(EnumType.STRING)
    @Column(name = "format_type")
    private FormatType formatType;

    @ManyToOne
    @JoinColumn(name = "entity_key")
    @NotNull
    private Property property;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;
}
