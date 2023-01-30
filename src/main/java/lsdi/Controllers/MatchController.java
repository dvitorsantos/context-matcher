package lsdi.Controllers;

import lsdi.Constants.ObjectTypes;
import lsdi.DataTransferObjects.EventProcessNetworkRequest;
import lsdi.DataTransferObjects.TaggedObjectResponse;
import lsdi.Entities.Rule;
import lsdi.Exceptions.MatchNotFoundException;
import lsdi.Services.TaggerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MatchController {
    @Autowired
    TaggerService taggerService;

    @PostMapping("/findMatch")
    public String findMatch(@RequestBody EventProcessNetworkRequest epn) {
        try {
            List<Rule> rules = epn.getRules();

            Map<String, TaggedObjectResponse[]> matchingObjects = this.findMatchingObjects(rules);

            //print list
            rules.forEach(rule -> {
                    System.out.println("Rule: " + rule.toString());
                    System.out.println("Matching Objects: " + Arrays.toString(matchingObjects.get(rule.getName())));
                });

            return Arrays.toString(matchingObjects.entrySet().toArray());
        } catch (MatchNotFoundException matchNotFoundException) {
            return matchNotFoundException.getMessage();
        }
    }

    public Map<String, TaggedObjectResponse[]> findMatchingObjects(List<Rule> rules) throws MatchNotFoundException {
        return new ArrayList<>(rules).stream()
                .collect(Collectors.toMap(Rule::getName, rule -> {
                    //get all objects that match the rule
                    String tagExpression = rule.getTagFilter();
                    TaggedObjectResponse[] taggedObjects = taggerService.getTaggedObjectByTagExpression(tagExpression);

                    //filter out objects that are not of the correct type
                    taggedObjects = Arrays.stream(taggedObjects)
                            .filter(taggedObject ->
                                    taggedObject.getType().equals(ObjectTypes.ObjectTypes.get(rule.getLevel())))
                            .toArray(TaggedObjectResponse[]::new);

                    //throw MatchNotFoundException if no objects are found
                    if (taggedObjects.length == 0)
                        throw new MatchNotFoundException("No matching objects found for rule: " + rule.getName());

                    return taggerService.getTaggedObjectByTagExpression(tagExpression);
                }));
    }
}
