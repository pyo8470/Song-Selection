package Selection.fret.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {
    @GetMapping("")
    public String mainPage(){
        return "Welcome to Main page";
    }
    @GetMapping("/hi")
    public String hi(){
        return "hi";
    }
}
