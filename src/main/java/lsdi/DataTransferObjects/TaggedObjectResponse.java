package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Models.Node;

import java.util.Map;

@Data
public class TaggedObjectResponse {
    private String uuid;
    private String type;
    private Map<String, String> tags;
    private String namespace;

    public Node toNode() {
        return new Node(this.uuid, this.type, this.namespace, this.tags);
    }
}
