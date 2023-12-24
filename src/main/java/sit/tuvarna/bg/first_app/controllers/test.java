package sit.tuvarna.bg.first_app.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class test {


    @GetMapping("/")
    public String home(){
        return "itworks";
    }


}
