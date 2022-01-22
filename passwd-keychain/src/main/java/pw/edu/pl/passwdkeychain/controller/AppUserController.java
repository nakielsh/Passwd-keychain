package pw.edu.pl.passwdkeychain.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.service.AppUserService;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/home")
    public String showPasswords(Model model) {
        List<Password> passwords = appUserService.showPasswords();
        List<Password> copiedPasswords = List.copyOf(passwords);

        model.addAttribute("passwords", copiedPasswords);
        return "passwords-view";
    }
}
