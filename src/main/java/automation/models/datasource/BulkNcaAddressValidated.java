package automation.models.datasource;

import automation.data_setup.Phaker;
import automation.enums.datasource.NcaAddressTypeEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "bulk_nca_address_validated")
public class BulkNcaAddressValidated extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "bulkNcaAddressValidated")
    private BulkCitizenValidated bulkCitizenValidated;

    @Column(name = "bulk_nca_address_type")
    private NcaAddressTypeEnum bulkNcaAddressType;

    @Column(name = "bulk_nca_block_no", length = 10)
    @Size(max = 10)
    private String bulkNcaBlockNo;

    @Column(name = "bulk_nca_street_code", length = 6)
    @Size(max = 6)
    private String bulkNcaStreetCode;

    @Column(name = "bulk_nca_level_no", length = 3)
    @Size(max = 3)
    private String bulkNcaLevelNo;

    @Column(name = "bulk_nca_unit_no", length = 5)
    @Size(max = 5)
    private String bulkNcaUnitNo;

    @Column(name = "bulk_nca_postal_code", length = 4)
    @Size(max = 4)
    private String bulkNcaPostalCode;

    @Column(name = "bulk_nca_new_postal_code", length = 6)
    @Size(max = 6)
    private String bulkNcaNewPostalCode;

    public static BulkNcaAddressValidated create() {
        return build(NcaAddressTypeEnum.pick());
    }

    private static BulkNcaAddressValidated build(NcaAddressTypeEnum ncaAddressTypeEnum) {
        return BulkNcaAddressValidated.builder()
                .bulkNcaAddressType(ncaAddressTypeEnum)
                .bulkNcaBlockNo(Phaker.validBlockNo())
                .bulkNcaLevelNo(Phaker.validFloorNo())
                .bulkNcaUnitNo(Phaker.validUnitNo())
                .bulkNcaNewPostalCode(Phaker.validPostalCode())
                .bulkNcaPostalCode(Phaker.validOldPostalCode())
                .build();
    }
}
