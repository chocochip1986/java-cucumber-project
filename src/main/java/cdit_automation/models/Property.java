package cdit_automation.models;

import cdit_automation.models.embeddables.DbTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "property")
@SuperBuilder
public class Property extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @Embedded
    private DbTemporalData dbTemporalData;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<AnnualValue> annualValue;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<PropertyDetail> propertyDetail;

    @JsonIgnore
    @OneToMany(mappedBy = "identifier.propertyEntity", cascade = CascadeType.ALL)
    private List<PersonProperty> personProperty;
}
