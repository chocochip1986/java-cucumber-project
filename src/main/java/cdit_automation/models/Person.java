package cdit_automation.models;

import cdit_automation.enums.RestrictedEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "person")
public class Person extends AbstractEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "restricted")
    private RestrictedEnum restricted;

    public static Person create() {
        return Person.builder().restricted(RestrictedEnum.NORMAL).build();
    }
}
