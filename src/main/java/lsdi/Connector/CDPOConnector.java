package lsdi.Connector;

import lsdi.Entities.Rule;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CDPOConnector {
    private final String url = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUrl() {
        return this.url;
    }

    public Rule getRuleByUuid(String uuid) {
        String requestUrl = this.getUrl() + "/rules/" + uuid;
        return restTemplate.getForObject(requestUrl, Rule.class);
    }
}
