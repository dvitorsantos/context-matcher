package lsdi.Services;

import lsdi.DataTransferObjects.TagExpressionRequest;
import lsdi.DataTransferObjects.TaggedObjectResponse;

import lsdi.Exceptions.TaggerException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaggerService {
    private final String url = "http://localhost:8180/tagger";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUrl() {
        return this.url;
    }

    public TaggedObjectResponse[] getTaggedObject() throws TaggerException {
        String requestUrl = this.getUrl() + "/taggedObject";

        try {
            return restTemplate.getForObject(requestUrl, TaggedObjectResponse[].class);
        } catch (Exception e) {
            throw new TaggerException("Tagger service error: " + e.getMessage());
        }
    }

    public TaggedObjectResponse[] getTaggedObjectByTagExpression(String tagExpression) throws TaggerException {
        String requestUrl = this.getUrl() + "/taggedObject/tagExpression";

        TagExpressionRequest tagExpressionRequest = new TagExpressionRequest();
        tagExpressionRequest.setExpression(tagExpression);

        try {
            return restTemplate.postForObject(requestUrl, tagExpressionRequest, TaggedObjectResponse[].class);
        } catch (Exception e) {
            throw new TaggerException("Tagger service error: " + e.getMessage());
        }
    }
}
