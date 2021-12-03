package pw.edu.pl.passwdkeychain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.repo.AppUserRepo;
import pw.edu.pl.passwdkeychain.repo.PasswordRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepo appUserRepo;
    private final PasswordRepo passwordRepo;

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("Saving new AppUser: {} to the database", appUser.getName());
        return appUserRepo.save(appUser);
    }

    @Override
    public Password savePassword(Password password) {
        log.info("Saving new password from service: {} to the database", password.getService());
        return passwordRepo.save(password);
    }

    @Override
    public void addPasswordToAppUser(String username, Long passwordId) {
        log.info("Adding password with id: {} to the AppUser: {}", passwordId, username);
        AppUser appUser = appUserRepo.findAppUserByUsername(username);
        Password password = passwordRepo.findPasswordById(passwordId);
        appUser.getSavedPasswords().add(password);
    }

    @Override
    public AppUser getAppUser(String username) {
        log.info("Fetching user: {}", username);
        return appUserRepo.findAppUserByUsername(username);
    }

    @Override
    public List<AppUser> getAppUsers() {
        log.info("Fetching all users");
        return appUserRepo.findAll();
    }
}
