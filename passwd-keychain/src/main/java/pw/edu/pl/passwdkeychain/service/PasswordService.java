package pw.edu.pl.passwdkeychain.service;

import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.dto.PasswordDTO;

public interface PasswordService {

    Password savePassword(Password password, String masterPassword, AppUser appUser);
    Password getPassword(Long id) throws IllegalAccessException;
    boolean deletePassword(Long id) throws IllegalAccessException;
    Password editPassword(PasswordDTO passwordDTO) throws IllegalAccessException;
    String decodePassword(Password password, String masterPassword) throws IllegalAccessException;
    Password createPassword(PasswordDTO passwordDTO);
    boolean isEqualToMasterPassword(String masterPassword);
}
