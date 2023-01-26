package lsdi.Services;

import lsdi.DataTransferObjects.TaggedObjectResponse;
import org.apache.coyote.Response;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

@PropertySource("classpath:application.properties")
public class TaggerService {
    private final String url = "http://localhost:8180/tagger";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUrl() {
        return this.url;
    }

    public TaggedObjectResponse[] getTaggedObject() {
        String requestUrl = this.url + "/taggedObject";
        return restTemplate.getForObject(requestUrl, TaggedObjectResponse[].class);
    }
}
