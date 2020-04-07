package cdit_automation.data_helpers.datasource;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.datasource.FileStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.FileDetail;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
@Setter
@Builder
public class FileReceivedDataDto {
    @NotNull
    private String filePath;

    @NotNull
    private Long fileDetailId;

    @NotNull private Double fileSize;

    @NotNull private String hash;

    @NotNull private Timestamp receivedTimestamp;

    @NotNull private FileStatusEnum fileStatusEnum;

    private final DateTimeFormatter DATETIME_FORMATTER_YYYYMMDD =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public boolean isNotValid() {
        return !isValid();
    }

    public boolean isValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<FileReceivedDataDto>> violations =
                validator.validate(this);
        return violations.isEmpty();
    }

    public String toJsonAsString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        if ( isNotValid() ) {
            throw new TestFailException("FileReceivedDataDto object must be valid before converting to a json object!");
        }
        Map<String, String> requestBodyKeyValuePairs = new HashMap<String, String>() {{
            put("filePath", filePath);
            put("fileDetailId", fileDetailId.toString());
            put("fileSize", fileSize.toString());
            put("hash", hash);
            put("receivedTimestamp", receivedTimestamp.toLocalDateTime().toLocalDate().toString());
            put("fileStatusEnum", fileStatusEnum.toString());
        }};

        JSONObject httpBody = createJsonObject(requestBodyKeyValuePairs);

        return httpBody;
    }

    private JSONObject createJsonObject(Map<String, String> bodyParams) {
        JSONObject httpBody = new JSONObject();
        try {
            for ( Map.Entry<String, String> bodyEntry : bodyParams.entrySet() ) {
                httpBody.put(bodyEntry.getKey(), bodyEntry.getValue());
            }
        } catch ( JSONException e ) {
            throw new TestFailException("Unable to create http request body!");
        }
        return httpBody;
    }

    public static FileReceivedDataDto createOk(FileDetail fileDetail, File file, Timestamp timestamp) {
        Timestamp receivedTimestamp = timestamp;
        if ( receivedTimestamp == null ) {
            receivedTimestamp = Timestamp.valueOf(LocalDate.now().atStartOfDay().withNano(0));
        }
        return create(fileDetail, file, receivedTimestamp, FileStatusEnum.OK);
    }

    public static FileReceivedDataDto create(FileDetail fileDetail, File file, Timestamp receivedTimestamp, FileStatusEnum fileStatusEnum) {
        return FileReceivedDataDto.builder()
                .fileDetailId(fileDetail.getId())
                .filePath(file.getAbsolutePath())
                .fileSize(10.0)
                .hash(Phaker.fakeMd5())
                .receivedTimestamp(receivedTimestamp)
                .fileStatusEnum(fileStatusEnum)
                .build();
    }
}
