package cdit_automation.models.datasource;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "reasonableness_check_statistic")
@Builder
public class ReasonablenessCheckStatistic extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @NotNull
    @Column(name = "data_item")
    private String dataItem;

    @NotNull
    @Column(name = "data_item_value")
    private String dataItemValue;

    @Column(name = "data_collected_date")
    private Timestamp dataCollectedDate;

    public static ReasonablenessCheckStatistic create(String dataItem, String dataItemValue, Batch batch) {
        return ReasonablenessCheckStatistic.builder().batch(batch).dataItem(dataItem).dataItemValue(dataItemValue).dataCollectedDate(batch.getCreatedAt()).build();
    }
}
