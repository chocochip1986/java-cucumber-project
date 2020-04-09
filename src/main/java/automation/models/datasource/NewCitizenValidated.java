package automation.models.datasource;

import automation.enums.datasource.AddressIndicatorEnum;
import automation.enums.datasource.GenderEnum;
import automation.enums.datasource.InvalidAddressTagEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
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
import javax.validation.constraints.NotNull;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum genderEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_address_indicator")
    private AddressIndicatorEnum oldAddressIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_address_indicator")
    private AddressIndicatorEnum newAddressIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_invalid_address_tag")
    private InvalidAddressTagEnum newInvalidAddressTag;

    @Column(name = "date_of_address_change")
    private LocalDate dateOfAddressChange;

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

    @Column(name = "is_mappable")
    private Boolean isMappable;

    @Column(name = "new_citizen_record_details")
    private String recordDetailsValidated;

    @Column(name = "duplicate_record_marker")
    private Long duplicateRecordMarker;
}
