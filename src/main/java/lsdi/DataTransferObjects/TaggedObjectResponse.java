package lsdi.DataTransferObjects;

import lombok.Data;

import java.util.Map;

@Data
public class TaggedObjectResponse {
    private String uuid;
    private String type;
    private Map<String, String> tags;
    private String namespace;
}
