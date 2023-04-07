package lsdi.Controllers;

import lsdi.DataTransferObjects.ContextDataRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/context")
public class ContextController {
    @PostMapping("/new")
    public void newContext(@RequestBody ContextDataRequest contextData) {
        System.out.println("New context data received: " + contextData.toString());
    }
}
