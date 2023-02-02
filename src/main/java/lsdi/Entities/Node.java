package lsdi.Entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Node {
    @Id
    private String uuid;
    private String type;
    private Map<String, String> tags;
    private String namespace;
}
