package cdit_automation.models;

import cdit_automation.enums.NcaAddressTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "old_nca_address_validated")
public class OldNcaAddress extends AbstractValidated {
    @OneToOne(mappedBy = "oldNcaAddress")
    private NewCitizenValidated newCitizenValidated;

    @Column(name = "old_nca_address_type")
    private NcaAddressTypeEnum oldNcaAddressType;

    @Column(name = "old_nca_block_no", length = 10)
    @Size(max = 10)
    private String oldNcaBlockNo;

    @Column(name = "old_nca_street_code", length = 6)
    @Size(max = 6)
    private String oldNcaStreetCode;

    @Column(name = "old_nca_level_no", length = 3)
    @Size(max = 3)
    private String oldNcaLevelNo;

    @Column(name = "old_nca_unit_no", length = 5)
    @Size(max = 5)
    private String oldNcaUnitNo;

    @Column(name = "old_nca_postal_code", length = 4)
    @Size(max = 4)
    private String oldNcaPostalCode;

    @Column(name = "old_nca_new_postal_code", length = 6)
    @Size(max = 6)
    private String oldNcaNewPostalCode;
}
