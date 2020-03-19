package cdit_automation.models;

import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.GenderEnum;
import cdit_automation.enums.InvalidAddressTagEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "new_citizen_validated")
public class NewCitizenValidated extends AbstractValidated {
    @Column(name = "fin", length = 9)
    @Size(max = 9)
    private String fin;

    @Column(name = "nric", length = 9)
    @Size(min = 9, max = 9)
    private String nric;

    @Column(name = "name", length = 66)
    @Size(max = 66)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_address_indicator")
    private AddressIndicatorEnum oldAddressIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_invalid_address_tag")
    private InvalidAddressTagEnum oldInvalidAddressTag;

    @Column(name = "old_address_valid_till_date")
    private LocalDate oldAddressValidTillDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_address_indicator")
    private AddressIndicatorEnum newAddressIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_invalid_address_tag")
    private InvalidAddressTagEnum newInvalidAddressTag;

    @Column(name = "new_address_valid_from_date")
    private LocalDate newAddressValidFromDate;

    @Column(name = "nationality", length = 2)
    @Size(max = 2)
    private String nationality;

    @Column(name = "citizenship_attainment_issue_date")
    private LocalDate citizenshipAttainmentIssueDate;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "old_mha_address_id", referencedColumnName = "id")
    private OldMhaAddress oldMhaAddress;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "old_nca_address_id", referencedColumnName = "id")
    private OldNcaAddress oldNcaAddress;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "new_mha_address_id", referencedColumnName = "id")
    private NewMhaAddress newMhaAddress;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "new_nca_address_id", referencedColumnName = "id")
    private NewNcaAddress newNcaAddress;
}
