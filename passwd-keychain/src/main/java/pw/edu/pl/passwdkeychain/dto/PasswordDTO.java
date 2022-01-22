package pw.edu.pl.passwdkeychain.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pw.edu.pl.passwdkeychain.security.validation.FieldsValueMatch;
import pw.edu.pl.passwdkeychain.security.validation.ValidPassword;

@FieldsValueMatch.List({
    @FieldsValueMatch(
            field = "savedPassword",
            fieldMatch = "savedPasswordAgain",
            message = "Passwords do not match!")
})
@Getter
@Setter
public class PasswordDTO {

    private Long id;

    @NotNull private String service;

    @ValidPassword private String savedPassword;

    @NotNull private String savedPasswordAgain;

    @NotNull private String masterPassword;
}
