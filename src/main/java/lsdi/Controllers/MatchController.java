package lsdi.Controllers;

import lsdi.DataTransferObjects.TaggedObjectResponse;
import lsdi.Services.TaggerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class MatchController {
    @PostMapping("/findMatch")
    public String match() {
        TaggerService taggerService = new TaggerService();
        TaggedObjectResponse[] taggedObjectResponse = taggerService.getTaggedObject();
        return Arrays.toString(taggedObjectResponse);
    }
}
