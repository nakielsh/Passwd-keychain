package pw.edu.pl.passwdkeychain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectionController {
    @GetMapping("")
    public String redirectToHome() {
        return "redirect:/home";
    }
}
