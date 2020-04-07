package cdit_automation.models.datasource;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "error_message")
public class ErrorMessage extends AbstractEntity  {
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
    private static final String STANDARD_ERROR_MESSAGE = "THIS IS AN ERROR MESSAGE";

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


    @Any(metaColumn = @Column(name = "validated_type"))
    @AnyMetaDef(
            idType = "long",
            metaType = "string",
            metaValues = {
//                    @MetaValue(
//                            targetEntity = PropertyValidated.class,
//                            value = ValidatedTypes.PROPERTY),
//                    @MetaValue(
//                            targetEntity = NoInteractionValidated.class,
//                            value = ValidatedTypes.NO_INTERACTION),
//                    @MetaValue(
//                            targetEntity = ChangeAddressValidated.class,
//                            value = ValidatedTypes.CHANGE_ADDRESS),
//                    @MetaValue(
//                            targetEntity = NewCitizenValidated.class,
//                            value = ValidatedTypes.NEW_CITIZEN),
                    @MetaValue(
                            targetEntity = DeathDateValidated.class,
                            value = ValidatedTypes.DEATH_DATE)
//                    @MetaValue(
//                            targetEntity = PersonDetailChangeValidated.class,
//                            value = ValidatedTypes.PERSON_DETAIL_CHANGE),
            })
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "validated_id")
    private AbstractValidated validatedData;

    @NotNull
    @Column(name = "message")
    private String message;

    public static ErrorMessage createErrorMsgForValidatedRecord(AbstractValidated abstractValidated, Batch batch) {
        return build(batch, null, abstractValidated, STANDARD_ERROR_MESSAGE);
    }

    public static ErrorMessage createErrorMsgForValidatedRecord(AbstractValidated abstractValidated, Batch batch, String message) {
        return build(batch, null, abstractValidated, message);
    }

    public static ErrorMessage createErrorMsgForIncomingRecord(IncomingRecord incomingRecord, Batch batch) {
        return build(batch, incomingRecord, null, STANDARD_ERROR_MESSAGE);
    }

    public static ErrorMessage createErrorMsgForIncomingRecord(IncomingRecord incomingRecord, Batch batch, String message) {
        return build(batch, incomingRecord, null, message);
    }

    public static ErrorMessage create(Batch batch, IncomingRecord incomingRecord, AbstractValidated abstractValidated, String message) {
        return build(batch, incomingRecord, abstractValidated, message);
    }

    private static ErrorMessage build(Batch batch, IncomingRecord incomingRecord, AbstractValidated abstractValidated, String message) {
        return ErrorMessage.builder()
                .batch(batch)
                .incomingRecord(incomingRecord)
                .validatedData(abstractValidated)
                .message(message)
                .build();
    }
}
