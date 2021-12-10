package pw.edu.pl.passwdkeychain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.dto.PasswordDTO;
import pw.edu.pl.passwdkeychain.service.AppUserService;
import pw.edu.pl.passwdkeychain.service.PasswordService;

@Controller
@RequiredArgsConstructor
public class PasswordController {

//    private final AppUserService appUserService;
    private final PasswordService passwordService;

    @RequestMapping("/password/delete/{id}")
    public String deletePassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = false;
        try {
            isDeleted = passwordService.deletePassword(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Successfully deleted password");
        }

        return "redirect:/home";
    }

    @ModelAttribute("passwordDTO")
    public PasswordDTO passwordDTO() {
        return new PasswordDTO();
    }

    @GetMapping("/password/add")
    public String addPassword(@ModelAttribute("passwordDTO") PasswordDTO passwordDTO, Model model) {
        model.addAttribute("passwordDTO", passwordDTO);
        return "password-add";
    }

    @PostMapping("/password/add")
    public String addPassword(@Validated PasswordDTO passwordDTO, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("passwordDTO", passwordDTO);
        if (!passwordService.isEqualToMasterPassword(passwordDTO.getMasterPassword())) {
            redirectAttributes.addFlashAttribute("error", "Wrong master password");
            return "redirect:/password/add";
        } else {
            Password password = passwordService.createPassword(passwordDTO);
            redirectAttributes.addFlashAttribute("success", "Added password to service: " + password.getService() );
            return "redirect:/home";
        }
    }

    @GetMapping("/password/show/{id}")
    public String showPassword(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        Password password;
        try {
            password = passwordService.getPassword(id);

            if (!model.containsAttribute("password"))
                model.addAttribute("password", password);
            if (!model.containsAttribute("isPasswordDecoded"))
                model.addAttribute("isPasswordDecoded", false);

        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "password-show";
    }

    @RequestMapping("/password/show/{id}")
    public String showPassword(@PathVariable Long id, @RequestParam("masterPassword") String masterPassword, Model model, RedirectAttributes redirectAttributes) {
        boolean isPasswordDecoded = false;
        try {
            Password password = passwordService.getPassword(id);

                String actualPassword = passwordService.decodePassword(password, masterPassword);
                password.setActualPassword(actualPassword);

                redirectAttributes.addFlashAttribute("password", password);
                isPasswordDecoded = true;


        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("isPasswordDecoded", isPasswordDecoded);
        return "redirect:/password/show/" + id;
    }

    @GetMapping("/password/edit/{id}")
    public String editPassword(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        Password password;
        try {
            password = passwordService.getPassword(id);

            if (!model.containsAttribute("password"))
                model.addAttribute("password", password);

        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "password-edit";
    }

    @RequestMapping("/password/edit/{id}")
    public String showPasswordToEdit(@PathVariable Long id, @Validated PasswordDTO passwordDTO, RedirectAttributes redirectAttributes) {

        if (!passwordService.isEqualToMasterPassword(passwordDTO.getMasterPassword())) {
            redirectAttributes.addFlashAttribute("error", "Wrong master password");
            return "redirect:/password/edit/" + id;
        } else {
            try {
                passwordDTO.setId(id);
                Password password = passwordService.editPassword(passwordDTO);

                redirectAttributes.addFlashAttribute("password", password);

            } catch (IllegalAccessException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }

        return "redirect:/home";

    }



}
