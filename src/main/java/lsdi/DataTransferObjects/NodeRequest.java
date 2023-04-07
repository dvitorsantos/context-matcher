package lsdi.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Models.Node;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeRequest {
    private String uuid;
    private String type;
    private String namespace = "default";
    private Map<String, String> tags;

    public Node toEntity() {
        return new Node(uuid, type, namespace, tags);
    }
}
