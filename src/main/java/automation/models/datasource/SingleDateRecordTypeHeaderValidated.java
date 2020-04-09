package automation.models.datasource;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "single_date_record_type_header_validated")
public class SingleDateRecordTypeHeaderValidated extends AbstractValidated {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "batch_id")
  @NotNull
  private Batch batch;

  @Column(name = "record_type")
  private int recordType;

  @Column(name = "extraction_date")
  private LocalDate extractionDate;
}
