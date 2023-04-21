package academy.devdojo.springbootessentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringbootEssentialsApplication {

    public static void main(String[] args) {

        System.out.println("vai que vai");

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(passwordEncoder.encode("academy"));



        SpringApplication.run(SpringbootEssentialsApplication.class, args);
    }

}