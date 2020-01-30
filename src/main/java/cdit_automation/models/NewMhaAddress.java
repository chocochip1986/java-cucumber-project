package cdit_automation.models;

import cdit_automation.enums.MhaAddressTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Builder
@Table(name = "new_mha_address_validated")
public class NewMhaAddress extends AbstractValidated {
    @OneToOne(mappedBy = "newMhaAddress")
    private NewCitizenValidated newCitizenValidated;

    @Column(name = "new_mha_address_type")
    private MhaAddressTypeEnum newMhaAddressType;

    @Column(name = "new_mha_block_no", length = 10)
    @Size(max = 10)
    private String newMhaBlockNo;

    @Column(name = "new_mha_street_name", length = 32)
    @Size(max = 32)
    private String newMhaStreetName;

    @Column(name = "new_mha_floor_no", length = 2)
    @Size(max = 2)
    private String newMhaFloorNo;

    @Column(name = "new_mha_unit_no", length = 5)
    @Size(max = 5)
    private String newMhaUnitNo;

    @Column(name = "new_mha_building_name", length = 30)
    @Size(max = 30)
    private String newMhaBuildingName;

    @Column(name = "new_mha_postal_code", length = 4)
    @Size(max = 4)
    private String newMhaPostalCode;

    @Column(name = "new_mha_new_postal_code", length = 6)
    @Size(max = 6)
    private String newMhaNewPostalCode;
}
