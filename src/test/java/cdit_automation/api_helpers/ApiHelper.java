package cdit_automation.api_helpers;

import cdit_automation.configuration.TestManager;
import cdit_automation.data_setup.Phaker;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ApiHelper {

    @Autowired TestManager testManager;

    private RestTemplate restService;

    @Autowired
    public ApiHelper () {
        restService = new RestTemplate();
    }

    public void sendCallToTriggerOutgoingIrasAiJob(@Nullable File file, @NotNull LocalDate date) {
        String requestParams = "date="+date.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
        if ( file != null ) {
            requestParams = "filePath="+file.getAbsolutePath()+"/"+"&"+requestParams;
        }

        String url = "http://"+testManager.getTestEnv().getDatasourceUrl()+":"+testManager.getTestEnv().getDatasourcePort()+"/egress/iras/ai/bulk?"+requestParams;

        getCall(url);
    }

    public void sendCallToTriggerOutgoingIrasTriMonthlyAiJob(@NotNull LocalDate date, @Nullable File file, @NotNull boolean isFirst) {
        String requestParams = "date=" + date.format(Phaker.DATETIME_FORMATTER_YYYYMMDD) + "&isFirstTriMonthly=" + (isFirst ? "true" : "false");
        if ( file != null ) {
            requestParams = "filePath="+file.getAbsolutePath()+"/"+"&"+requestParams;
        }
        String url = "http://"+testManager.getTestEnv().getDatasourceUrl()+":"+testManager.getTestEnv().getDatasourcePort()+"/egress/iras/ai/triMonthly?"+requestParams;
        getCall(url);
    }

    public void sendCallToTriggerBatchJob(@NotNull FileReceived fileReceived) {
        String url = "http://"+testManager.getTestEnv().getDatasourceUrl()+":"+testManager.getTestEnv().getDatasourcePort()+"/receiver/validateFile";

        MultiValueMap<String, String> httpHeader = new LinkedMultiValueMap<>();
        httpHeader.put("Content-Type", Arrays.asList(MediaType.APPLICATION_JSON_VALUE));

        Map<String, String> requestBodyKeyValuePairs = new HashMap<String, String>() {{
            put("fileReceivedId", fileReceived.getId().toString());
        }};

        JSONObject httpBody = addToBody(requestBodyKeyValuePairs);

        postCall(url, httpHeader, httpBody.toString());
    }

    private void getCall(@NotNull String url) {
        ResponseEntity<String> httpResponse = restService.getForEntity(url, String.class);

        processHttpResponse(httpResponse, url);
    }

    private void postCall(@NotNull String url, @Nullable MultiValueMap<String, String> httpHeader, @Nullable String httpBody) {
        HttpEntity<String> entity = new HttpEntity(httpBody, httpHeader);
        ResponseEntity<String> httpResponse = restService.postForEntity(url, entity, String.class);

        processHttpResponse(httpResponse, url);
    }

    private void processHttpResponse(ResponseEntity<String> httpResponse, String url) {
        if ( isResonseOk(httpResponse) ) {
            //Do nothing
        } else {
            String errorMsg = "Unable to send call to url:"+url+System.lineSeparator();
            errorMsg+="Http Response code: "+httpResponse.getStatusCode()+System.lineSeparator();
            log.error(errorMsg);
            throw new TestFailException(errorMsg);
        }
    }

    private boolean isResonseOk(ResponseEntity<String> httpResponse) {
        return  httpResponse.getStatusCode().equals(HttpStatus.OK);
    }

    private JSONObject addToBody(Map<String, String> bodyParams) {
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
}
