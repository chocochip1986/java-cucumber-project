package automation.models.datasource.view;

import automation.enums.datasource.BatchStatusEnum;
import automation.enums.datasource.FileTypeEnum;
import automation.models.datasource.AbstractEntity;
import automation.models.datasource.Batch;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "view_reasonableness_check_statistic")
@Builder
public class ReasonablenessCheckStatisticView extends AbstractEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileTypeEnum fileType;

    @NotNull
    @Column(name = "data_item")
    private String dataItem;

    @NotNull
    @Column(name = "data_item_value")
    private String dataItemValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "batch_status")
    private BatchStatusEnum batchStatus;

    @Column(name = "data_collected_date")
    private Timestamp dataCollectedDate;
}
