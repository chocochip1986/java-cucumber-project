package cdit_automation.models;

import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.Gender;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.YesNoTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "bulk_citizen_validated")
public class BulkCitizenValidated extends AbstractValidated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

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
    private Gender gender;

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
