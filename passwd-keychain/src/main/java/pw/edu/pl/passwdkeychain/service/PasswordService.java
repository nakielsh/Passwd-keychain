package pw.edu.pl.passwdkeychain.service;

import pw.edu.pl.passwdkeychain.domain.Password;

public interface PasswordService {

    boolean deletePassword(Long id) throws IllegalAccessException;
    Password editPassword(Password password) throws IllegalAccessException;
    String decodePassword(Password password, String masterPassword) throws IllegalAccessException;
}
