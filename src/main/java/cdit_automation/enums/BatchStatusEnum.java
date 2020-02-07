package cdit_automation.enums;

import java.util.Random;

public enum BatchStatusEnum {
    INIT("INIT"),
    INIT_ERROR("INIT_ERROR"),
    CHECKING_FILE("CHECKING_FILE"),
    LINE_CHECKED("LINE_CHECKED"),
    LINE_ERROR("LINE_ERROR"),
    FILE_CHECKED("FILE_CHECKED"),
    FILE_ERROR("FILE_ERROR"),
    RAW_DATA("RAW_DATA"),
    RAW_DATA_ERROR("RAW_DATA_ERROR"),
    BULK_INCOMING_RECORD_CHECK("BULK_INCOMING_RECORD_CHECK"),
    BULK_CHECK_VALIDATED_DATA("BULK_CHECK_VALIDATED_DATA"),
    BULK_CHECK_VALIDATION_ERROR("BULK_CHECK_VALIDATION_ERROR"),
    VALIDATED_TO_PREPARED_DATA("VALIDATED_TO_PREPARED_DATA"),
    VALIDATED_TO_PREPARED_ERROR("VALIDATED_TO_PREPARED_ERROR"),
    ERROR_RATE("ERROR_RATE"),
    ERROR_RATE_ERROR("ERROR_RATE_ERROR"),
    MAPPED_DATA("MAPPED_DATA"),
    MAPPING_ERROR("MAPPING_ERROR"),
    BULK_MAPPED_DATA("BULK_MAPPED_DATA"),
    BULK_MAPPED_DATA_ERROR("BULK_MAPPED_DATA_ERROR"),
    CLEANUP("CLEANUP"),
    CLEANUP_ERROR("CLEANUP_ERROR"),
    SENT_DATA("SENT_DATA"),
    SENDING_ERROR("SENDING_ERROR");

    private static BatchStatusEnum[] ERROR_STATUSES = new BatchStatusEnum[]{
            INIT_ERROR,
            LINE_ERROR,
            FILE_ERROR,
            RAW_DATA_ERROR,
            BULK_CHECK_VALIDATION_ERROR,
            VALIDATED_TO_PREPARED_ERROR,
            ERROR_RATE_ERROR,
            MAPPING_ERROR,
            BULK_MAPPED_DATA_ERROR,
            CLEANUP_ERROR,
            SENDING_ERROR
    };

    private String value;

    BatchStatusEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static BatchStatusEnum randomValidBatchStatusEnum() {
        return BatchStatusEnum.values()[new Random().nextInt(BatchStatusEnum.values().length)];
    }

    public static boolean isBatchStatusAnErrorStatus(BatchStatusEnum batchStatusEnum){
        for(BatchStatusEnum batchStatus : ERROR_STATUSES) {
            if ( batchStatusEnum.equals(batchStatus) ) {
                return true;
            }
        }
        return false;
    }
}
