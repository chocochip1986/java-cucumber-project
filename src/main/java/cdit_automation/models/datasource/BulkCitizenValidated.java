package cdit_automation.models.datasource;

import cdit_automation.enums.datasource.AddressIndicatorEnum;
import cdit_automation.enums.datasource.GenderEnum;
import cdit_automation.enums.datasource.NationalityEnum;
import cdit_automation.enums.datasource.YesNoTypeEnum;
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
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "bulk_citizen_validated")
public class BulkCitizenValidated extends AbstractValidated {
    @Column(name = "nric", length = 9)
    @Size(min = 9, max = 9)
    private String nric;

    @Column(name = "fin", length = 9)
    @Size(max = 9)
    private String fin;

    @Column(name = "name", length = 66)
    @Size(max = 66)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_death")
    private LocalDate dateOfdeath;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_indicator")
    private AddressIndicatorEnum addressIndicator;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bulk_nca_address_id", referencedColumnName = "id")
    private BulkNcaAddressValidated bulkNcaAddressValidated;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bulk_mha_address_id", referencedColumnName = "id")
    private BulkMhaAddressValidated bulkMhaAddressValidated;

    @Column(name = "nationality", length = 2)
    @Enumerated(EnumType.STRING)
    private NationalityEnum nationality;

    @Column(name = "citizenship_attainment_issue_date")
    private LocalDate citizenshipAttainmentIssueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "dual_citizenship")
    private YesNoTypeEnum dualCitizenship;
}
