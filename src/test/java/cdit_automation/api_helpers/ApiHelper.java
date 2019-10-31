package cdit_automation.api_helpers;

import cdit_automation.configuration.TestManager;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class ApiHelper {

    @Autowired TestManager testManager;

    private RestTemplate restService;

    @Autowired
    public ApiHelper () {
        restService = new RestTemplate();
    }

    public void sendCallToTriggerBatchJob(@NotNull FileTypeEnum fileType, @NotNull FileReceived fileReceived) {
        String url = "http://"+testManager.getTestEnv().getDatasourceUrl()+":"+testManager.getTestEnv().getDatasourcePort()+"/receiver/validateFile?fileName="+fileType.getValue()+"&fileReceivedId="+fileReceived.getId().toString();
        postCall(url);
    }

    private void postCall(@NotNull String url) {
        HttpEntity<String> entity = new HttpEntity<>(null, null);
        ResponseEntity<String> httpResponse = restService.postForEntity(url, entity, String.class);
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
}
