package pw.edu.pl.passwdkeychain.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
public class PasswordDTO {

    private Long id;

    private String service;

    private String savedPassword;
    private String savedPasswordAgain;

    private String masterPassword;

}
