package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.MhaAddressTypeEnum;
import cdit_automation.utilities.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "bulk_mha_address_validated")
public class BulkMhaAddressValidated extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "bulkMhaAddressValidated")
    private BulkCitizenValidated bulkCitizenValidated;

    @Column(name = "bulk_mha_address_type")
    private MhaAddressTypeEnum bulkMhaAddressType;

    @Column(name = "bulk_mha_block_no", length = 10)
    @Size(max = 10)
    private String bulkMhaBlockNo;

    @Column(name = "bulk_mha_street_name", length = 32)
    @Size(max = 32)
    private String bulkMhaStreetName;

    @Column(name = "bulk_mha_floor_no", length = 2)
    @Size(max = 2)
    private String bulkMhaFloorNo;

    @Column(name = "bulk_mha_unit_no", length = 5)
    @Size(max = 5)
    private String bulkMhaUnitNo;

    @Column(name = "bulk_mha_building_name", length = 30)
    @Size(max = 30)
    private String bulkMhaBuildingName;

    @Column(name = "bulk_mha_postal_code", length = 4)
    @Size(max = 4)
    private String bulkMhaPostalCode;

    @Column(name = "bulk_mha_new_postal_code", length = 6)
    @Size(max = 6)
    private String bulkMhaNewPostalCode;

    public static BulkMhaAddressValidated createOverseas() {
        return build(MhaAddressTypeEnum.OVERSEAS_ADDRESS);
    }

    public static BulkMhaAddressValidated create() {
        return build(MhaAddressTypeEnum.pick());
    }

    private static BulkMhaAddressValidated build(MhaAddressTypeEnum mhaAddressTypeEnum) {
        return BulkMhaAddressValidated.builder()
                .bulkMhaAddressType(mhaAddressTypeEnum)
                .bulkMhaBlockNo(Phaker.validBlockNo())
                .bulkMhaFloorNo(Phaker.validFloorNo())
                .bulkMhaStreetName(StringUtils.leftPad(Phaker.validStreetName(), 32))
                .bulkMhaBuildingName(StringUtils.leftPad(Phaker.validBuildingName(), 30))
                .bulkMhaPostalCode(Phaker.validOldPostalCode())
                .bulkMhaNewPostalCode(Phaker.validPostalCode())
                .build();
    }
}
