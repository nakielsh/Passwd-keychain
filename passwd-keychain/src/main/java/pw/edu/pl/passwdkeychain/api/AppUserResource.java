package pw.edu.pl.passwdkeychain.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.service.AppUserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserResource {

    private final AppUserService appUserService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(appUserService.getAppUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveAppUser(appUser));
    }

    @PostMapping("/password/save")
    public ResponseEntity<Password> savePassword(@RequestBody Password password) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/password/save").toUriString());
        return ResponseEntity.created(uri).body(appUserService.savePassword(password));
    }

    @PostMapping("/password/addtouser")
    public ResponseEntity<?> addPasswordtoUser(@RequestBody PasswordToUserForm form) {
        appUserService.addPasswordToAppUser(form.getUsername(), form.getPasswordId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/passwords")
    public ResponseEntity<List<Password>> showPasswords() {
        return ResponseEntity.ok().body(appUserService.showPasswords());
    }
}

@Data
class PasswordToUserForm{
    private String username;
    private Long passwordId;
}
