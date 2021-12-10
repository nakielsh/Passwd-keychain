package pw.edu.pl.passwdkeychain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.service.AppUserService;
import pw.edu.pl.passwdkeychain.service.PasswordService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordService passwordService;

    @GetMapping("/home")
    public String showPasswords(Model model) {
        List<Password> passwords = appUserService.showPasswords();
        List<Password> copiedPasswords = List.copyOf(passwords);

        model.addAttribute("passwords", copiedPasswords);
        return "passwords-view";
    }
}
