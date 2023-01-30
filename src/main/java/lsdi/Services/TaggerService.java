package lsdi.Services;

import lsdi.DataTransferObjects.TagExpressionRequest;
import lsdi.DataTransferObjects.TaggedObjectResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaggerService {
    private final String url = "http://localhost:8180/tagger";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUrl() {
        return this.url;
    }

    public TaggedObjectResponse[] getTaggedObject() {
        String requestUrl = this.getUrl() + "/taggedObject";

        return restTemplate.getForObject(requestUrl, TaggedObjectResponse[].class);
    }

    public TaggedObjectResponse[] getTaggedObjectByTagExpression(String tagExpression) {
        String requestUrl = this.getUrl() + "/taggedObject/tagExpression";

        TagExpressionRequest tagExpressionRequest = new TagExpressionRequest();
        tagExpressionRequest.setExpression(tagExpression);

        return restTemplate.postForObject(requestUrl, tagExpressionRequest, TaggedObjectResponse[].class);
    }
}
