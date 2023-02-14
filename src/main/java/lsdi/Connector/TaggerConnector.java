package lsdi.Connector;

import lsdi.DataTransferObjects.NodeRequest;
import lsdi.DataTransferObjects.TagExpressionRequest;
import lsdi.DataTransferObjects.TaggedObjectResponse;

import lsdi.Exceptions.TaggerException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TaggerConnector {
    private final String url = "http://localhost:8180/tagger";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUrl() {
        return this.url;
    }

    public TaggedObjectResponse[] getAllTaggedObjects() throws TaggerException {
        String requestUrl = this.getUrl() + "/taggedObject";

        try {
            return restTemplate.getForObject(requestUrl, TaggedObjectResponse[].class);
        } catch (Exception e) {
            throw new TaggerException("Tagger service error: " + e.getMessage());
        }
    }

    public Optional<TaggedObjectResponse> getTaggedObjectByUuid(String uuid) throws TaggerException {
        String requestUrl = this.getUrl() + "/taggedObject/" + uuid;

        try {
            return Optional.ofNullable(restTemplate.getForObject(requestUrl, TaggedObjectResponse.class));
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

    public void putTagsInObject(NodeRequest nodeRequest) throws TaggerException {
        String requestUrl = this.getUrl() + "/taggedObject";

        try {
            restTemplate.put(requestUrl, nodeRequest, NodeRequest.class);
        } catch (Exception e) {
            throw new TaggerException("Tagger service error: " + e.getMessage());
        }
    }
}
