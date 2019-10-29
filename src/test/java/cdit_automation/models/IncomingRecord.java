package cdit_automation.models;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileContentCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Builder
@Table(name = "incoming_record")
public class IncomingRecord {
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
}
