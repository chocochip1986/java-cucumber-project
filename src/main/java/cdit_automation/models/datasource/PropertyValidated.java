package cdit_automation.models.datasource;

import cdit_automation.enums.datasource.AddressType;
import cdit_automation.enums.datasource.FlatType;
import cdit_automation.enums.datasource.HdbActionCode;
import cdit_automation.enums.datasource.IsRentalFlat;
import cdit_automation.enums.datasource.PropertyType;
import cdit_automation.enums.datasource.ShopHouseLQ;
import cdit_automation.enums.datasource.ShopHouseTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
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
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "property_validated")
public class PropertyValidated extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // (cascade = CascadeType.ALL)
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @Column(name = "action_code")
    @Enumerated(EnumType.STRING)
    private HdbActionCode actionCode;

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "floor")
    private String floor;

    @Column(name = "unit")
    private String unit;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "new_postal_code")
    private String newPostalCode;

    @Column(name = "street_code")
    @Size(max = 6)
    private String streetCode;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "property_type")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(name = "flat_type")
    @Enumerated(EnumType.STRING)
    private FlatType flatType;

    @Column(name = "shop_house_tag")
    @Enumerated(EnumType.STRING)
    private ShopHouseTag shopHouseTag;

    @Column(name = "shop_house_lq")
    @Enumerated(EnumType.STRING)
    private ShopHouseLQ shopHouseLq;

    @Column(name = "is_rental_flat")
    @Enumerated(EnumType.STRING)
    private IsRentalFlat isRentalFlat;
}
