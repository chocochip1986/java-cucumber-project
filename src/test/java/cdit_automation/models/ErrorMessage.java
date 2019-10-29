package cdit_automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "error_message")
public class ErrorMessage {
    public static class ValidatedTypes {
        public static final String PROPERTY = "property";
        public static final String NO_INTERACTION = "noInteraction";
        public static final String CHANGE_ADDRESS = "changeAddress";
        public static final String NEW_CITIZEN = "newCitizen";
        public static final String DEATH_DATE = "deathDate";
        public static final String PERSON_DETAIL_CHANGE = "personDetailChange";

        private ValidatedTypes() {
            // Not Needed
        }
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // (cascade = {CascadeType.MERGE})
    @NotNull
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @OneToOne
    @JoinColumn(name = "incoming_record_id")
    private IncomingRecord incomingRecord;

//    @Any(metaColumn = @Column(name = "validated_type"))
//    @AnyMetaDef(
//            idType = "long",
//            metaType = "string",
//            metaValues = {
//                    @MetaValue(targetEntity = PropertyValidated.class, value = ValidatedTypes.PROPERTY),
//                    @MetaValue(
//                            targetEntity = NoInteractionValidated.class,
//                            value = ValidatedTypes.NO_INTERACTION),
//                    @MetaValue(
//                            targetEntity = ChangeAddressValidated.class,
//                            value = ValidatedTypes.CHANGE_ADDRESS),
//                    @MetaValue(targetEntity = NewCitizenValidated.class, value = ValidatedTypes.NEW_CITIZEN),
//                    @MetaValue(targetEntity = DeathDateValidated.class, value = ValidatedTypes.DEATH_DATE),
//                    @MetaValue(
//                            targetEntity = PersonDetailChangeValidated.class,
//                            value = ValidatedTypes.PERSON_DETAIL_CHANGE),
//            })
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
//    @JoinColumn(name = "validated_id")
//    private EntityInterface validatedData;

    @NotNull
    @Column(name = "message")
    private String message;
}
