package pw.edu.pl.passwdkeychain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

        return "redirect:/passwords";
    }

}
