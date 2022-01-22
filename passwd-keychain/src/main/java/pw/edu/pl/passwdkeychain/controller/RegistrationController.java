package pw.edu.pl.passwdkeychain.controller;

import java.util.ArrayList;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.dto.AppUserDTO;
import pw.edu.pl.passwdkeychain.service.AppUserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final AppUserService appUserService;

    @ModelAttribute("appUserDTO")
    public AppUserDTO appUserDTO() {
        return new AppUserDTO();
    }

    @GetMapping("/registration")
    public String register(Model model) {
        model.addAttribute("appUserDTO", new AppUserDTO());
        return "register";
    }

    @PostMapping("/registration")
    public String register(
            @Valid @ModelAttribute("appUserDTO") AppUserDTO appUserDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            if (result.hasFieldErrors()) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            } else {
                redirectAttributes.addFlashAttribute(
                        "error", result.getAllErrors().get(0).getDefaultMessage());
            }
            return "redirect:/registration";
        }

        try {
            AppUser found = appUserService.getAppUser(appUserDTO.getUsername());
            if (found != null) {
                redirectAttributes.addFlashAttribute("error", "Username already taken");
                return "redirect:/registration";
            }
        } catch (UsernameNotFoundException e) {
            appUserService.saveAppUser(
                    new AppUser(
                            null,
                            appUserDTO.getName(),
                            appUserDTO.getUsername(),
                            appUserDTO.getPassword(),
                            appUserDTO.getMasterPassword(),
                            new ArrayList<>()));
            redirectAttributes.addFlashAttribute(
                    "success",
                    "You have successfully created account for user: " + appUserDTO.getUsername());
        }

        return "redirect:/login";
    }
}
