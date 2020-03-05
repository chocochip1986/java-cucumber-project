package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileContentCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "incoming_record")
public class IncomingRecord extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @NotNull
    @Column(name = "seq_no")
    private int seqNo;

    @NotNull
    @Column(columnDefinition = "CLOB", name = "record_details")
    private String recordDetails;

    @Nullable
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BatchStatusEnum status;

    @NotNull
    @Column(name = "file_content_code")
    @Enumerated(EnumType.STRING)
    private FileContentCodeEnum fileContentCode;

    @Nullable
    @Column(name = "reason_code")
    private String reasonCode;

    @NotNull
    @Column(name = "entered")
    private LocalDateTime entered;

    public static IncomingRecord createFooterRAWError(Batch batch, String recordDetails) {
        return build(batch, recordDetails, BatchStatusEnum.RAW_DATA_ERROR, FileContentCodeEnum.FOOTER, Integer.valueOf(Phaker.validNumber(2)));
    }

    public static IncomingRecord createHeaderRAWError(Batch batch, String recordDetails) {
        return build(batch, recordDetails, BatchStatusEnum.RAW_DATA_ERROR, FileContentCodeEnum.HEADER, Integer.valueOf(Phaker.validNumber(2)));
    }

    public static IncomingRecord createBodyRAWError(Batch batch, String recordDetails) {
        return build(batch, recordDetails, BatchStatusEnum.RAW_DATA_ERROR, FileContentCodeEnum.BODY, Integer.valueOf(Phaker.validNumber(2)));
    }

    public static IncomingRecord createFooterRAW(Batch batch, String recordDetails) {
        return build(batch, recordDetails, BatchStatusEnum.RAW_DATA, FileContentCodeEnum.FOOTER, Integer.valueOf(Phaker.validNumber(2)));
    }

    public static IncomingRecord createHeaderRAW(Batch batch, String recordDetails) {
        return build(batch, recordDetails, BatchStatusEnum.RAW_DATA, FileContentCodeEnum.HEADER, Integer.valueOf(Phaker.validNumber(2)));
    }

    public static IncomingRecord createBodyRAW(Batch batch, String recordDetails) {
        return build(batch, recordDetails, BatchStatusEnum.RAW_DATA, FileContentCodeEnum.BODY, Integer.valueOf(Phaker.validNumber(2)));
    }

    private static IncomingRecord build(Batch batch, String recordDetails, BatchStatusEnum batchStatusEnum, FileContentCodeEnum fileContentCodeEnum, int seqNo) {
        return IncomingRecord.builder()
                .batch(batch)
                .recordDetails(recordDetails)
                .status(batchStatusEnum)
                .fileContentCode(fileContentCodeEnum)
                .seqNo(seqNo)
                .entered(batch.getCreatedAt().toLocalDateTime())
                .build();
    }
}
