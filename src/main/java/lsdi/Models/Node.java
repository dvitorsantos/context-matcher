package lsdi.Models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    @Id
    private String uuid;
    private String type;
    private String namespace;
    private Map<String, String> tags;
}
