package pw.edu.pl.passwdkeychain.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pw.edu.pl.passwdkeychain.security.validation.FieldsValueMatch;
import pw.edu.pl.passwdkeychain.security.validation.ValidPassword;

@FieldsValueMatch.List({
    @FieldsValueMatch(
            field = "password",
            fieldMatch = "matchingPassword",
            message = "Passwords do not match!"),
    @FieldsValueMatch(
            field = "masterPassword",
            fieldMatch = "matchingMasterPassword",
            message = "Master Passwords do not match!")
})
@Getter
@Setter
public class AppUserDTO {

    private Long id;

    @NotNull private String name;

    @NotNull private String username;

    @NotNull @ValidPassword private String password;

    @NotNull private String matchingPassword;

    @NotNull @ValidPassword private String masterPassword;

    @NotNull private String matchingMasterPassword;
}
