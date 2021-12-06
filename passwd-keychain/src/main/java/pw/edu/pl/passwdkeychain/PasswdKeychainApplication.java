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
import pw.edu.pl.passwdkeychain.service.PasswordService;
import pw.edu.pl.passwdkeychain.service.PasswordServiceImpl;

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
	CommandLineRunner run(AppUserService appUserService, PasswordService passwordService) {
		return args -> {
			Password pass1 = new Password( "Faceboook", "test");
			Password pass2 = new Password("Twitter", "test");
			Password pass3 = new Password( "Instagram", "instapass");

			AppUser lisek = appUserService.saveAppUser(new AppUser(null, "Lisek Ogrodniczek", "lisogr", "hasloliska", "masterpassliska", new ArrayList<>()));

			passwordService.savePassword(pass1, "masterpassliska", lisek);
			passwordService.savePassword(pass2, "masterpassliska", lisek);
			passwordService.savePassword(pass3, "masterpassliska", lisek);

//			appUserService.addPasswordToAppUser("lisogr", pass1.getId());
//			appUserService.addPasswordToAppUser("lisogr", pass2.getId());
//			appUserService.addPasswordToAppUser("lisogr", pass3.getId());
		};
	}

}
