package cdit_automation.api_helpers.datasource;

import cdit_automation.configuration.datasource.TestEnv;
import cdit_automation.data_helpers.datasource.FileReceivedDataDto;
import cdit_automation.data_setup.Phaker;
import cdit_automation.models.datasource.FileReceived;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    public static final String ACTUATOR_HEATH_ENDPOINT = "/actuator/health";

    private static String urlSuffix = "/v1";

    @Autowired
    public ApiHelper () {
        super();
    }

    public void sendCallToTriggerOutgoingIrasAiJob(@NotNull LocalDate date) {
        String requestParams = "date="+date.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);

        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+ urlSuffix +"/egress/iras/ai/bulk?"+requestParams;

        getCall(url);
    }

    public void sendCallToTriggerOutgoingIrasTriMonthlyAiJob(@NotNull LocalDate date, @NotNull boolean isCurrentYAExcluded) {
        String requestParams = "date=" + date.format(Phaker.DATETIME_FORMATTER_YYYYMMDD) + "&excludeCurrentYA=" + (isCurrentYAExcluded ? "true" : "false");
        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+ urlSuffix +"/egress/iras/ai/thriceMonthly?"+requestParams;
        getCall(url);
    }

    public void sendCallToCreateFileReceivedRecord(FileReceivedDataDto fileReceivedDataDto) {
        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+ urlSuffix + "/fileReceived";
        MultiValueMap<String, String> httpHeader = new LinkedMultiValueMap<>();
        httpHeader.put("Content-Type", Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

        postCall(url, httpHeader, fileReceivedDataDto.toJsonAsString());
    }

    public void sendCallToTriggerBatchJob(@NotNull FileReceived fileReceived) {
        String url = "http://"+testEnv.getDatasourceUrl()+":"+testEnv.getDatasourcePort()+ urlSuffix + "/validateFile";

        MultiValueMap<String, String> httpHeader = new LinkedMultiValueMap<>();
        httpHeader.put("Content-Type", Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

        Map<String, String> requestBodyKeyValuePairs = new HashMap<>();
        requestBodyKeyValuePairs.put("fileReceivedId", fileReceived.getId().toString());
        requestBodyKeyValuePairs.put("isIgnoreErrorRate", "false");

        JSONObject httpBody = addToBody(requestBodyKeyValuePairs);

        postCall(url, httpHeader, httpBody.toString());
    }

  public void sendCallToTriggerOutgoingIrasFirstBulkJob(
      @NotNull LocalDate localDate, @NotNull String yearOfAssessment) {
    String url =
        "http://"
            + testEnv.getDatasourceUrl()
            + ":"
            + testEnv.getDatasourcePort()
            + urlSuffix
            + "/egress/iras/ai/firstBulk?date="
            + localDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD)
            + "&yearOfAssessment="
            + yearOfAssessment;
    getCall(url);
  }

    /**
     * Send a GET request to Datasource to ensure environment is up with a response of 200/OK
     * TODO: Have to implement assertion to response containing exactly this: {"status":"UP"}
     */
  public void sendCallToVerifyEnvironmentHealthIsUp() {
      final String url = testEnv.getEnv().equals(TestEnv.Env.QA) ?
            String.format("%s%s", testEnv.getDatasourceUrl(), ACTUATOR_HEATH_ENDPOINT) :
            String.format("http://%s:%s%s", testEnv.getDatasourceUrl(), testEnv.getDatasourcePort(), ACTUATOR_HEATH_ENDPOINT);

      log.info(String.format("Test Automation: %s %s", "Calling endpoint", url));
        getCall(url);
  }
}
