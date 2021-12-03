package pw.edu.pl.passwdkeychain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.edu.pl.passwdkeychain.domain.Password;

public interface PasswordRepo extends JpaRepository<Password, Long> {
    Password findPasswordById(Long id);
}
