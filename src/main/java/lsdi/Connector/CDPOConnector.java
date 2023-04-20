package lsdi.Connector;

import lsdi.Entities.Rule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CDPOConnector {
    @Value("${cdpo.url}")
    private String url;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUrl() {
        return "http://cdpo:8080";
    }

    public Rule getRuleByUuid(String uuid) {
        String requestUrl = this.getUrl() + "/rules/" + uuid;
        return restTemplate.getForObject(requestUrl, Rule.class);
    }

    public void deployRule(String uuid) {
        String requestUrl = this.getUrl() + "/deploy/rule/" + uuid;
        restTemplate.postForObject(requestUrl, null, String.class);
    }

    public void undeployRule(String uuid) {
        String requestUrl = this.getUrl() + "/undeploy/rule/" + uuid;
        restTemplate.postForObject(requestUrl, null, String.class);
    }
}
