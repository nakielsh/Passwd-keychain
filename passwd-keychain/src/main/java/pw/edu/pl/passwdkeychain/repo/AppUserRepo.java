package pw.edu.pl.passwdkeychain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.edu.pl.passwdkeychain.domain.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByUsername(String username);
}
