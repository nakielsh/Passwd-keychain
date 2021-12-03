package pw.edu.pl.passwdkeychain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.edu.pl.passwdkeychain.domain.AppUser;
import pw.edu.pl.passwdkeychain.domain.Password;
import pw.edu.pl.passwdkeychain.service.AppUserService;

import java.util.ArrayList;

@SpringBootApplication
public class PasswdKeychainApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswdKeychainApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder(13);
	}

	@Bean
	CommandLineRunner run(AppUserService appUserService) {
		return args -> {
			Password pass1 = appUserService.savePassword(new Password( null, "Faceboook", "test"));
			Password pass2 = appUserService.savePassword(new Password( null, "Twitter", "test"));
			Password pass3 = appUserService.savePassword(new Password( null, "Instagram", "instapass"));

			appUserService.saveAppUser(new AppUser(null, "Lisek Ogrodniczek", "lisogr", "hasloliska", "pasterpasslika", new ArrayList<>()));

			appUserService.addPasswordToAppUser("lisogr", pass1.getId());
			appUserService.addPasswordToAppUser("lisogr", pass2.getId());
			appUserService.addPasswordToAppUser("lisogr", pass3.getId());
		};
	}

}
