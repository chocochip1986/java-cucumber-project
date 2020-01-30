package cdit_automation.models;

import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.InvalidAddressTagEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "change_address_validated")
public class ChangeAddressValidated extends AbstractValidated {

    @Column(name = "nric")
    private String nric;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_address_indicator")
    private AddressIndicatorEnum oldAddressIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_address_indicator")
    private AddressIndicatorEnum newAddressIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_invalid_address_tag")
    private InvalidAddressTagEnum newInvalidAddressTag;

    @Column(name = "address_changed_date")
    private LocalDate addressChangedDate;

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

    @Column(name = "change_addr_record_details")
    private String recordDetails;

    @Column(name = "duplicate_record_marker")
    private int duplicateRecordMarker;
}
