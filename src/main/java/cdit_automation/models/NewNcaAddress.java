package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.NcaAddressTypeEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "new_nca_address_validated")
public class NewNcaAddress extends AbstractValidated {

    @OneToOne(mappedBy = "newNcaAddress")
    private NewCitizenValidated newCitizenValidated;

    @Column(name = "new_nca_address_type")
    private NcaAddressTypeEnum newNcaAddressType;

    @Column(name = "new_nca_block_no", length = 10)
    @Size(max = 10)
    private String newNcaBlockNo;

    @Column(name = "new_nca_street_code", length = 6)
    @Size(max = 6)
    private String newNcaStreetCode;

    @Column(name = "new_nca_level_no", length = 3)
    @Size(max = 3)
    private String newNcaLevelNo;

    @Column(name = "new_nca_unit_no", length = 5)
    @Size(max = 5)
    private String newNcaUnitNo;

    @Column(name = "new_nca_postal_code", length = 4)
    @Size(max = 4)
    private String newNcaPostalCode;

    @Column(name = "new_nca_new_postal_code", length = 6)
    @Size(max = 6)
    private String newNcaNewPostalCode;

    public static NewNcaAddress create(){
        return NewNcaAddress.builder()
                .newNcaAddressType(NcaAddressTypeEnum.RESIDENTIAL)
                .newNcaBlockNo(Phaker.validBlockNo())
                .newNcaStreetCode(Phaker.validPostalCode())
                .newNcaLevelNo(Phaker.validFloorNo())
                .newNcaUnitNo(Phaker.validUnitNo())
                .newNcaPostalCode(Phaker.validOldPostalCode())
                .newNcaNewPostalCode(Phaker.validPostalCode())
                .build();
    }
}
