package pw.edu.pl.passwdkeychain.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.service.AppUserService;

import java.util.List;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/passwords")
    public String showPasswords(Model model) {
        List<Password> passwords = appUserService.showPasswords();
        model.addAttribute("passwords", passwords);
        return "passwords-view";
    }
}
