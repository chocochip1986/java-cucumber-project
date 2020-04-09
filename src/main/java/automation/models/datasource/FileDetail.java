package automation.models.datasource;

import automation.enums.datasource.AgencyEnum;
import automation.enums.datasource.FileFrequencyEnum;
import automation.enums.datasource.FileTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "file_detail")
public class FileDetail extends AbstractEntity  {
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

    @NotNull
    @Column(name = "file_name")
    private String fileName;

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

    @NotNull
    @Column(name = "actual_footer_size")
    private Integer actualFooterSize;

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
                actualFooterSize = 9;
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
                actualFooterSize = 9;
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
                actualFooterSize = 9;
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
                actualFooterSize = 0;
                footerFillerSize = 0;
                header = true;
                footer = false;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_NO_INTERACTION:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 16;
                bodySize = 25;
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_NEW_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                actualFooterSize = 9;
                headerSize = 8;
                bodySize = 292;
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
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.YEARLY;
                break;
            case MHA_DEATH_DATE:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 17;
                actualFooterSize = 9;
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
                actualFooterSize = 0;
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
                actualFooterSize = 9;
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
                actualFooterSize = 11;
                footerFillerSize = 21;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_DUAL_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 9;
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_CEASED_CITIZEN:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 85;
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case MHA_PERSON_DETAIL_CHANGE:
                agency = AgencyEnum.MHA;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 84;
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case IRAS_BULK_AI:
                agency = AgencyEnum.IRAS;
                hasFooterFiller = true;
                headerSize = 50;
                bodySize = 50;
                actualFooterSize = 9;
                footerFillerSize = 1;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.YEARLY;
                break;
            case IRAS_THRICE_MONTHLY_AI:
                agency = AgencyEnum.IRAS;
                hasFooterFiller = true;
                headerSize = 50;
                bodySize = 50;
                actualFooterSize = 9;
                footerFillerSize = 1;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.THRICE_MONTHLY;
                break;
            case CPFB_CLASSIFIED_ACCOUNT:
                agency = AgencyEnum.CPFB;
                hasFooterFiller = false;
                headerSize = 30;
                bodySize = 30;
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.DAILY;
                break;
            case CPFB_NURSING_HOME:
                agency = AgencyEnum.CPFB;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 23;
                actualFooterSize = 9;
                footerFillerSize = 0;
                header = true;
                footer = true;
                frequency = FileFrequencyEnum.MONTHLY;
                break;
            case CPFB_LORONG_BUANGKOK:
                agency = AgencyEnum.CPFB;
                hasFooterFiller = false;
                headerSize = 8;
                bodySize = 20;
                actualFooterSize = 9;
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
