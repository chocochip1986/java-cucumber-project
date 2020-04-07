package cdit_automation.models.datasource;

import cdit_automation.enums.datasource.FormatType;
import cdit_automation.enums.datasource.PreparedPropertyTypeEnum;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

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
    private PreparedPropertyTypeEnum propertyType;

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

    // Attribute is for Automation use only. Will not be persisted.
    @Transient
    private String addressType;

    public static PropertyDetail createNcaHdb(Batch batch, String unitNo, String blockNo, String floorNo,
                                              String buildingName, String streetCode, String oldPostalCode, String postalCode,
                                              Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, null, streetCode, oldPostalCode, postalCode, PreparedPropertyTypeEnum.FIVE_ROOM_HDB, FormatType.NCA, property, biTemporalData);
    }

    public static PropertyDetail createNcaNonHdb(Batch batch, String unitNo, String blockNo, String floorNo,
                                              String buildingName, String streetCode, String oldPostalCode, String postalCode,
                                              Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, null, streetCode, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.NCA, property, biTemporalData);
    }

    public static PropertyDetail createNcaPrewar(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetCode, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, null, streetCode, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.NCA, property, biTemporalData);
    }

    public static PropertyDetail createNcaIsland(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetCode, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, null, streetCode, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.NCA, property, biTemporalData);
    }

    public static PropertyDetail createNcaKampong(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetCode, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, null, streetCode, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.NCA, property, biTemporalData);
    }

    public static PropertyDetail createMhaHdb(Batch batch, String unitNo, String blockNo, String floorNo,
                                              String buildingName, String streetName, String oldPostalCode, String postalCode,
                                              Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, streetName, null, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.MHA, property, biTemporalData);
    }

    public static PropertyDetail createMhaNonHdb(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetName, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, streetName, null, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.MHA, property, biTemporalData);
    }

    public static PropertyDetail createMhaPrewar(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetName, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, streetName, null, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.MHA, property, biTemporalData);
    }

    public static PropertyDetail createMhaIsland(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetName, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, streetName, null, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.MHA, property, biTemporalData);
    }

    public static PropertyDetail createMhaKampong(Batch batch, String unitNo, String blockNo, String floorNo,
                                                 String buildingName, String streetName, String oldPostalCode, String postalCode,
                                                 Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, streetName, null, oldPostalCode, postalCode, PreparedPropertyTypeEnum.PRIVATE_PROPERTY, FormatType.MHA, property, biTemporalData);
    }

    public static PropertyDetail create(Batch batch, String unitNo, String blockNo, String floorNo,
                                        String buildingName, String streetName, String streetCode,
                                        String oldPostalCode, String postalCode, PreparedPropertyTypeEnum propertyType,
                                        FormatType formatType, Property property, BiTemporalData biTemporalData) {
        return build(batch, unitNo, blockNo, floorNo, buildingName, streetName, streetCode, oldPostalCode, postalCode, propertyType, formatType, property, biTemporalData);
    }

    private static PropertyDetail build(Batch batch, String unitNo, String blockNo, String floorNo,
                                        String buildingName, String streetName, String streetCode,
                                        String oldPostalCode, String postalCode, PreparedPropertyTypeEnum propertyType,
                                        FormatType formatType, Property property, BiTemporalData biTemporalData) {
        return PropertyDetail.builder().batch(batch).unit(unitNo).blockNumber(blockNo).floor(floorNo)
                .buildingName(buildingName).streetName(streetName).streetCode(streetCode).postalCode(oldPostalCode).newPostalCode(postalCode)
                .propertyType(propertyType).formatType(formatType)
                .property(property).biTemporalData(biTemporalData)
                .build();
    }
}
