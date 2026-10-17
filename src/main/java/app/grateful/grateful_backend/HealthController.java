package app.grateful.grateful_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping
    public String ping(){
        return "Grateful backend is awake";
    }
}
