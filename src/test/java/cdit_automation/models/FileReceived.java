package cdit_automation.models;

import cdit_automation.enums.FileStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "file_received")
public class FileReceived extends AbstractEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "file_detail_id")
    private FileDetail fileDetail;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Double fileSize;

    @Column(name = "hash")
    private String hash;

    @Column(name = "received_date")
    private Timestamp receivedTimestamp;

    @Column(name = "file_status")
    private FileStatusEnum fileStatusEnum;

    @JsonIgnore
    @OneToMany(mappedBy = "fileReceived", cascade = CascadeType.ALL)
    private List<Batch> batches;
}
