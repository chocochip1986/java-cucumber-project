package automation.api_helpers.datasource;

import automation.configuration.datasource.TestEnv;
import automation.configuration.TestManager;
import automation.exceptions.TestFailException;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class AbstractApiHelper {
    protected @Autowired
    TestManager testManager;

    protected @Autowired
    TestEnv testEnv;

    protected RestTemplate restService;

    protected AbstractApiHelper() {
        restService = new RestTemplate();
    }

    protected void getCall(@NotNull String url) {
        ResponseEntity<String> httpResponse = restService.getForEntity(url, String.class);

        processHttpResponse(httpResponse, url);
    }

    protected void postCall(@NotNull String url, @Nullable MultiValueMap<String, String> httpHeader, @Nullable String httpBody) {
        HttpEntity<String> entity = new HttpEntity(httpBody, httpHeader);
        ResponseEntity<String> httpResponse = restService.postForEntity(url, entity, String.class);

        processHttpResponse(httpResponse, url);
    }

    protected void processHttpResponse(ResponseEntity<String> httpResponse, String url) {
        if ( isResonseOk(httpResponse) ) {
            //Do nothing
        } else {
            String errorMsg = "Unable to send call to url:"+url+System.lineSeparator();
            errorMsg+="Http Response code: "+httpResponse.getStatusCode()+System.lineSeparator();
            log.error(errorMsg);
            throw new TestFailException(errorMsg);
        }
    }

    protected boolean isResonseOk(ResponseEntity<String> httpResponse) {
        return  httpResponse.getStatusCode().equals(HttpStatus.OK);
    }

    protected JSONObject addToBody(Map<String, String> bodyParams) {
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
