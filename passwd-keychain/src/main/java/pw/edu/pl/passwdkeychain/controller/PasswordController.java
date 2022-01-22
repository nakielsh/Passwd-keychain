package pw.edu.pl.passwdkeychain.controller;

import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.dto.PasswordDTO;
import pw.edu.pl.passwdkeychain.service.PasswordService;

@Controller
@RequiredArgsConstructor
public class PasswordController {

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
    public String addPassword(Model model) {
        model.addAttribute("passwordDTO", new PasswordDTO());
        return "password-add";
    }

    @PostMapping("/password/add")
    public String addPassword(
            @Valid @ModelAttribute("passwordDTO") PasswordDTO passwordDTO,
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
            return "redirect:/password/add";
        }

        if (!passwordService.isEqualToMasterPassword(passwordDTO.getMasterPassword())) {
            redirectAttributes.addFlashAttribute("error", "Wrong master password");
            return "redirect:/password/add";
        } else {
            Password password = passwordService.createPassword(passwordDTO);
            redirectAttributes.addFlashAttribute(
                    "success", "Added password to service: " + password.getService());
            return "redirect:/home";
        }
    }

    @GetMapping("/password/show/{id}")
    public String showPassword(
            @PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        Password password;
        try {
            password = passwordService.getPassword(id);

            if (!model.containsAttribute("password")) model.addAttribute("password", password);
            if (!model.containsAttribute("isPasswordDecoded"))
                model.addAttribute("isPasswordDecoded", false);

        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "password-show";
    }

    @RequestMapping("/password/show/{id}")
    public String showPassword(
            @PathVariable Long id,
            @RequestParam("masterPassword") String masterPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
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
    public String editPassword(
            @PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        Password password;
        try {
            password = passwordService.getPassword(id);

            if (!model.containsAttribute("password")) model.addAttribute("password", password);

        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "password-edit";
    }

    @RequestMapping("/password/edit/{id}")
    public String showPasswordToEdit(
            @PathVariable Long id,
            @Valid @ModelAttribute("passwordDTO") PasswordDTO passwordDTO,
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
            return "redirect:/password/edit/" + id;
        }

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
