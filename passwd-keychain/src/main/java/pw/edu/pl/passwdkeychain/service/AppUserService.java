package pw.edu.pl.passwdkeychain.service;

import java.util.List;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;

public interface AppUserService {
    AppUser saveAppUser(AppUser appUser);

    AppUser getAppUser(String username);

    List<AppUser> getAppUsers();

    List<Password> showPasswords();

    AppUser getCurrentAppUser();
}
