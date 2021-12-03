package pw.edu.pl.passwdkeychain.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class AppUser {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String masterPassword;
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Password> savedPasswords = new ArrayList<>();
}
