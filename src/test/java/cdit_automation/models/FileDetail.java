package cdit_automation.models;

import cdit_automation.enums.AgencyEnum;
import cdit_automation.enums.FileFrequencyEnum;
import cdit_automation.enums.FileTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "file_detail")
public class FileDetail {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "agency")
    private AgencyEnum agency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileTypeEnum fileEnum;

    private Boolean header;
    private Boolean footer;

    @NotNull
    @Column(name = "has_footer_filler")
    private Boolean hasFooterFiller;

    @NotNull
    @Column(name = "header_size")
    private Integer headerSize;

    @NotNull
    @Column(name = "body_size")
    private Integer bodySize;

    @NotNull
    @Column(name = "footer_filler_size")
    private Integer footerFillerSize;

    @Column(name = "frequency")
    @Enumerated(EnumType.STRING)
    private FileFrequencyEnum frequency;

    @JsonIgnore
    @OneToMany(mappedBy = "fileDetail", cascade = CascadeType.ALL)
    private List<FileReceived> fileReceivedList;

    @SuppressWarnings("squid:S2637")
    public FileDetail(FileTypeEnum fileEnum) {
        this.fileEnum = fileEnum;

        switch (fileEnum) {
            case HDB_PROPERTY:
                agency = AgencyEnum.HDB;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 41;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MSF_PWD:
                agency = AgencyEnum.MSF;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 25;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MSF_TRANSITIONAL_SHELTER:
                agency = AgencyEnum.MSF;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 9;
                footerFillerSize = 9;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case IRAS_FORM_B:
                agency = AgencyEnum.IRAS;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 9;
                footerFillerSize = 0;
                header = true;
                footer = false;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_NO_INTERACTION:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 14;
                bodySize = 25;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_NEW_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 303;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_BULK_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 204;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_DEATH_DATE:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 17;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case IRAS_DECLARED_NTI:
                agency = AgencyEnum.IRAS;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 32;
                footerFillerSize = 0;
                header = true;
                footer = false;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_CHANGE_ADDRESS:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 200;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case SINGPOST_SELF_EMPLOYED:
                agency = AgencyEnum.SINGPOST;
                hasFooterFiller = true;
                headerSize = 9;
                bodySize = 317;
                footerFillerSize = 20;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_DUAL_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_CEASED_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 86;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_PERSON_DETAIL_CHANGE:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 84;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            default:
                break;
        }
    }
}
