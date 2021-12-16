package pw.edu.pl.passwdkeychain.dto;

import lombok.Getter;
import lombok.Setter;
import pw.edu.pl.passwdkeychain.security.validation.PasswordMatches;
import pw.edu.pl.passwdkeychain.security.validation.ValidPassword;

//@FieldsValueMatch.List({
//        @FieldsValueMatch(
//                field = "savedPassword",
//                fieldMatch = "savedPasswordAgain",
//                message = "Passwords do not match!"
//        )
//})
@PasswordMatches
@Getter
@Setter
public class PasswordDTO {

    private Long id;

    private String service;

    @ValidPassword
    private String savedPassword;

    private String savedPasswordAgain;

    private String masterPassword;
}
