package pw.edu.pl.passwdkeychain.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String service;
    @NonNull
    private String savedPassword;

    @Transient
    private String actualPassword;


}
