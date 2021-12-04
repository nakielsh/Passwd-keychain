package pw.edu.pl.passwdkeychain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.repo.AppUserRepo;
import pw.edu.pl.passwdkeychain.repo.PasswordRepo;
import pw.edu.pl.passwdkeychain.security.AES;

import javax.transaction.Transactional;

import static pw.edu.pl.passwdkeychain.security.AES.decrypt;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final AppUserRepo appUserRepo;
    private final PasswordRepo passwordRepo;
    private final PasswordEncoder passwordEncoder;
    private final AppUserService appUserService;

    @Override
    public boolean deletePassword(Long id) throws IllegalAccessException {
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        Password passwdToDelete = passwordRepo.findPasswordById(id);
        if (currentAppUser.getSavedPasswords().contains(passwdToDelete)) {
            currentAppUser.getSavedPasswords().remove(passwdToDelete);
            passwordRepo.delete(passwdToDelete);
            return true;
        } else {
            throw new IllegalAccessException("You can only delete your passwords");
        }
    }

    @Override
    public Password editPassword(Password password) throws IllegalAccessException {
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        Password passwdToEdit = passwordRepo.findPasswordById(password.getId());
        if (currentAppUser.getSavedPasswords().contains(passwdToEdit)) {
            passwdToEdit.setSavedPassword(password.getSavedPassword());
            passwdToEdit.setService(password.getService());
            return passwdToEdit;
        } else {
            throw new IllegalAccessException("You can only edit your passwords");
        }
    }

    @Override
    public String decodePassword(Password password, String masterPassword) throws IllegalAccessException {
        String realMasterPassword = appUserService.getCurrentAppUser().getMasterPassword();
        if (passwordEncoder.matches(masterPassword, realMasterPassword)) {
            return AES.decrypt(password.getSavedPassword(), realMasterPassword);
        } else {
            throw new IllegalAccessException("Wrong masterPassword");
        }
    }
}
