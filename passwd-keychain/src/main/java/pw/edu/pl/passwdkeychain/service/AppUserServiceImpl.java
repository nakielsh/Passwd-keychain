package pw.edu.pl.passwdkeychain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.repo.AppUserRepo;
import pw.edu.pl.passwdkeychain.repo.PasswordRepo;
import pw.edu.pl.passwdkeychain.security.AES;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepo appUserRepo;
    private final PasswordRepo passwordRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("Saving new AppUser: {} to the database", appUser.getName());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setMasterPassword(passwordEncoder.encode(appUser.getMasterPassword()));
        return appUserRepo.save(appUser);
    }

//    @Override
//    public Password savePassword(Password password) {
//        log.info("Saving new password from service: {} to the database", password.getService());
////        password.setSavedPassword(passwordEncoder.encode(password.getSavedPassword()));
//        return passwordRepo.save(password);
//    }
//
//    @Override
//    public void addPasswordToAppUser(String username, Long passwordId) {
//        log.info("Adding password with id: {} to the AppUser: {}", passwordId, username);
//        AppUser appUser = appUserRepo.findAppUserByUsername(username);
//        Password password = passwordRepo.findPasswordById(passwordId);
//
//        password.setSavedPassword(AES.encrypt(password.getSavedPassword(), appUser.getMasterPassword()));
//
//        appUser.getSavedPasswords().add(password);
//    }

    @Override
    public AppUser getAppUser(String username) {
        log.info("Fetching user: {}", username);
        AppUser appUser = appUserRepo.findAppUserByUsername(username);
        if(appUser == null) {
            log.info("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            return appUser;
        }
    }

    @Override
    public List<AppUser> getAppUsers() {
        log.info("Fetching all users");
        return appUserRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findAppUserByUsername(username);
        if(appUser == null) {
            log.info("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("User"));
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Password> showPasswords() {
        AppUser currentAppUser = getCurrentAppUser();
        return (List<Password>) currentAppUser.getSavedPasswords();
    }

    @Override
    public AppUser getCurrentAppUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = "";
        User principal;
        if (null != securityContext.getAuthentication()) {
            if (securityContext.getAuthentication().getPrincipal().getClass() == String.class) {
                username = (String) securityContext.getAuthentication().getPrincipal();

            } else {
                principal = (User) securityContext.getAuthentication().getPrincipal();
                username = principal.getUsername();

            }
        }
        return getAppUser(username);
    }
}
