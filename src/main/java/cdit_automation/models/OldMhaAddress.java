package cdit_automation.models;

import cdit_automation.enums.MhaAddressTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "old_mha_address_validated")
public class OldMhaAddress extends AbstractValidated {
    @OneToOne(mappedBy = "oldMhaAddress")
    private NewCitizenValidated newCitizenValidated;

    @Column(name = "old_mha_address_type")
    @NotNull
    private MhaAddressTypeEnum oldMhaAddressType;

    @Column(name = "old_mha_block_no", length = 10)
    @Size(max = 10)
    private String oldMhaBlockNo;

    @Column(name = "old_mha_street_name", length = 32)
    @Size(max = 32)
    private String oldMhaStreetName;

    @Column(name = "old_mha_floor_no", length = 2)
    @Size(max = 2)
    private String oldMhaFloorNo;

    @Column(name = "old_mha_unit_no", length = 5)
    @Size(max = 5)
    private String oldMhaUnitNo;

    @Column(name = "old_mha_building_name", length = 30)
    @Size(max = 30)
    private String oldMhaBuildingName;

    @Column(name = "old_mha_postal_code", length = 4)
    @Size(max = 4)
    private String oldMhaPostalCode;

    @Column(name = "old_mha_new_postal_code", length = 6)
    @Size(max = 6)
    private String oldMhaNewPostalCode;
}
