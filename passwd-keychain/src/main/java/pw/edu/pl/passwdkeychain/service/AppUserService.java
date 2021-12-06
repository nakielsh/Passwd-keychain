package pw.edu.pl.passwdkeychain.service;

import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;

import java.util.List;

public interface AppUserService {
    AppUser saveAppUser(AppUser appUser);
//    Password savePassword(Password password);
//    void addPasswordToAppUser(String username, Long passwordId);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
    List<Password> showPasswords();
    AppUser getCurrentAppUser();
}
