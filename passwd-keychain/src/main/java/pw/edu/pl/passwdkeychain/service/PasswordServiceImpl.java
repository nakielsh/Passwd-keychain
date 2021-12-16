package pw.edu.pl.passwdkeychain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.dto.PasswordDTO;
import pw.edu.pl.passwdkeychain.repo.AppUserRepo;
import pw.edu.pl.passwdkeychain.repo.PasswordRepo;
import pw.edu.pl.passwdkeychain.security.EncryptionDecryptionUtil;

import org.springframework.transaction.annotation.Transactional;


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
    public Password savePassword(Password password, String masterPassword, AppUser appUser) {
        log.info("Saving new password from service: {} to the database", password.getService());
        AppUser appUserDB = appUserRepo.findAppUserByUsername(appUser.getUsername());
        password.setSavedPassword(EncryptionDecryptionUtil.encrypt(masterPassword, password.getSavedPassword()));

        passwordRepo.save(password);
        appUserDB.getSavedPasswords().add(password);

        return password;
    }

    @Override
    public Password getPassword(Long id) throws IllegalAccessException {
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        Password passwdToDelete = passwordRepo.findPasswordById(id);
        if (currentAppUser.getSavedPasswords().contains(passwdToDelete)) {
            return passwordRepo.findPasswordById(id);
        } else {
            throw new IllegalAccessException("You can only get your passwords");
        }
    }

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
    public Password editPassword(PasswordDTO passwordDTO) throws IllegalAccessException {
        AppUser currentAppUser = appUserService.getCurrentAppUser();
        Password passwdToEdit = passwordRepo.findPasswordById(passwordDTO.getId());
        if (currentAppUser.getSavedPasswords().contains(passwdToEdit)) {
            passwdToEdit.setSavedPassword(EncryptionDecryptionUtil.encrypt(passwordDTO.getMasterPassword(), passwordDTO.getSavedPassword()));

            passwdToEdit.setService(passwordDTO.getService());
            return passwdToEdit;
        } else {
            throw new IllegalAccessException("You can only edit your passwords");
        }
    }

    @Override
    public String decodePassword(Password password, String masterPassword) throws IllegalAccessException {
        String realMasterPassword = appUserService.getCurrentAppUser().getMasterPassword();
        if (passwordEncoder.matches(masterPassword, realMasterPassword)) {
            return EncryptionDecryptionUtil.decrypt(masterPassword, password.getSavedPassword());
        } else {
            throw new IllegalAccessException("Wrong masterPassword");
        }
    }

    @Override
    public Password createPassword(PasswordDTO passwordDTO) {
        Password passwordToCreate = new Password();
        AppUser appUser = appUserService.getCurrentAppUser();

        passwordToCreate.setService(passwordDTO.getService());
        passwordToCreate.setSavedPassword(passwordDTO.getSavedPassword());

        return savePassword(passwordToCreate, passwordDTO.getMasterPassword(), appUser);
    }

    @Override
    public boolean isEqualToMasterPassword(String masterPassword) {
        AppUser appUser = appUserService.getCurrentAppUser();

        return passwordEncoder.matches(masterPassword, appUser.getMasterPassword());
    }
}
