package cdit_automation.api_helpers;

import cdit_automation.data_helpers.FileReceivedDataDto;
import cdit_automation.data_setup.Phaker;
import cdit_automation.models.FileReceived;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
public class ApiHelper extends AbstractApiHelper {

    @Autowired
    public ApiHelper () {
        super();
    }

    public void sendCallToTriggerOutgoingIrasAiJob(@Nullable File file, @NotNull LocalDate date) {
        String requestParams = "date="+date.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
        if ( file != null ) {
            requestParams = "filePath="+file.getAbsolutePath()+"/"+"&"+requestParams;
        }

        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+"/egress/iras/ai/bulk?"+requestParams;

        getCall(url);
    }

    public void sendCallToTriggerOutgoingIrasTriMonthlyAiJob(@NotNull LocalDate date, @Nullable File file, @NotNull boolean isFirst) {
        String requestParams = "date=" + date.format(Phaker.DATETIME_FORMATTER_YYYYMMDD) + "&isFirstTriMonthly=" + (isFirst ? "true" : "false");
        if ( file != null ) {
            requestParams = "filePath="+file.getAbsolutePath()+"/"+"&"+requestParams;
        }
        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+"/egress/iras/ai/thriceMonthly?"+requestParams;
        getCall(url);
    }

    public void sendCallToCreateFileReceivedRecord(FileReceivedDataDto fileReceivedDataDto) {
        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+"/v1/fileReceived";
        MultiValueMap<String, String> httpHeader = new LinkedMultiValueMap<>();
        httpHeader.put("Content-Type", Arrays.asList(MediaType.APPLICATION_JSON_VALUE));

        postCall(url, httpHeader, fileReceivedDataDto.toJsonAsString());
//        Map<String, String> requestBodyKeyValuePairs = new HashMap<>(); {{
//
//        }};
    }

    public void sendCallToTriggerBatchJob(@NotNull FileReceived fileReceived) {
        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+"/receiver/validateFile";

        MultiValueMap<String, String> httpHeader = new LinkedMultiValueMap<>();
        httpHeader.put("Content-Type", Arrays.asList(MediaType.APPLICATION_JSON_VALUE));

        Map<String, String> requestBodyKeyValuePairs = new HashMap<String, String>() {{
            put("fileReceivedId", fileReceived.getId().toString());
        }};

        JSONObject httpBody = addToBody(requestBodyKeyValuePairs);

        postCall(url, httpHeader, httpBody.toString());
    }
}
