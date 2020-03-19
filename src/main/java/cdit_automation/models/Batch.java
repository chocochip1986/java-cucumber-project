package cdit_automation.models;

import cdit_automation.enums.BatchStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "batch")
public class Batch extends AbstractEntity  {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Getter
    private BatchStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "file_received_id")
    private FileReceived fileReceived;

    @JsonIgnore
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<ErrorMessage> errorMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<IncomingRecord> incomingRecords;

    public static Batch create(BatchStatusEnum batchStatusEnum) {
        return Batch.builder()
                .status(batchStatusEnum)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    public static Batch create(BatchStatusEnum batchStatusEnum, Timestamp createdAt) {
        return Batch.builder()
                .status(batchStatusEnum)
                .createdAt(createdAt)
                .build();
    }

    public static Batch create(BatchStatusEnum batchStatusEnum, Timestamp createdAt, FileReceived fileReceived) {
        return Batch.builder()
                .status(batchStatusEnum)
                .createdAt(createdAt)
                .fileReceived(fileReceived)
                .build();
    }

    public static Batch createCompleted(Timestamp createdAt) {
        return Batch.builder().status(BatchStatusEnum.CLEANUP).createdAt(createdAt).build();
    }

    public static Batch createCompleted() {
        return create(BatchStatusEnum.CLEANUP);
    }
}
