package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.AddressIndicatorEnum;
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

    public static ChangeAddressValidated createOldNcaNewNca(Batch batch, OldNcaAddress oldNcaAddress, NewNcaAddress newNcaAddress) {
        return build(batch,
                Phaker.validNric(),
                AddressIndicatorEnum.NCA,
                AddressIndicatorEnum.NCA,
                InvalidAddressTagEnum.pick(),
                oldNcaAddress,
                newNcaAddress,
                null,
                null,
                Phaker.validPastDate());
    }

    public static ChangeAddressValidated createOldNcaNewMha(Batch batch, OldNcaAddress oldNcaAddress, NewMhaAddress newMhaAddress) {
        return build(batch,
                Phaker.validNric(),
                AddressIndicatorEnum.NCA,
                AddressIndicatorEnum.MHA_Z,
                InvalidAddressTagEnum.pick(),
                oldNcaAddress,
                null,
                null,
                newMhaAddress,
                Phaker.validPastDate());
    }

    public static ChangeAddressValidated createOldMhaNewNca(Batch batch, OldMhaAddress oldMhaAddress, NewNcaAddress newNcaAddress) {
        return build(batch,
                Phaker.validNric(),
                AddressIndicatorEnum.MHA_Z,
                AddressIndicatorEnum.NCA,
                InvalidAddressTagEnum.pick(),
                null,
                newNcaAddress,
                oldMhaAddress,
                null,
                Phaker.validPastDate());
    }

    public static ChangeAddressValidated createOldMhaNewMha(Batch batch, OldMhaAddress oldMhaAddress, NewMhaAddress newMhaAddress) {
        return build(batch,
                Phaker.validNric(),
                AddressIndicatorEnum.MHA_Z,
                AddressIndicatorEnum.MHA_Z,
                InvalidAddressTagEnum.pick(),
                null,
                null,
                oldMhaAddress,
                newMhaAddress,
                Phaker.validPastDate());
    }

    private static ChangeAddressValidated build(Batch batch,
                                                String nric,
                                                AddressIndicatorEnum oldAddressIndicatorEnum,
                                                AddressIndicatorEnum newAddressIndicatorEnum,
                                                InvalidAddressTagEnum invalidAddressTagEnum,
                                                OldNcaAddress oldNcaAddress,
                                                NewNcaAddress newNcaAddress,
                                                OldMhaAddress oldMhaAddress,
                                                NewMhaAddress newMhaAddress,
                                                LocalDate addressChangedDate) {
        return ChangeAddressValidated.builder()
                .batch(batch)
                .nric(nric)
                .oldAddressIndicator(oldAddressIndicatorEnum)
                .oldMhaAddress(oldMhaAddress)
                .oldNcaAddress(oldNcaAddress)
                .newAddressIndicator(newAddressIndicatorEnum)
                .newMhaAddress(newMhaAddress)
                .newNcaAddress(newNcaAddress)
                .newInvalidAddressTag(invalidAddressTagEnum)
                .addressChangedDate(addressChangedDate)
                .build();
    }
}
