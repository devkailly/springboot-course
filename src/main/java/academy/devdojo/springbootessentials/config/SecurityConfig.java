
package academy.devdojo.springbootessentials.config;

import academy.devdojo.springbootessentials.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final DevDojoUserDetailsService devDojoUserDetailsService;

    /**
     * Formas de autenticacao
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInterceptor
     * Dois processos na parte de segurança
     * Authentication -> Authorization
     * @param http
     * @return
     * @throws Exception
     */

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authCofig -> authCofig.anyRequest().permitAll()
                )

                .csrf().disable()
                .formLogin(Customizer.withDefaults());
        return http.build();
    }
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        UserDetails user = User.withUsername("devdojo")
//                .password(encoder.encode("bald"))
//                .roles("USER" , "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//
//    }

//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        log.info("Password Encoded {} ", passwordEncoder.encode("academy"));
//
//        auth.inMemoryAuthentication()
//                .withUser("william")
//                .password(passwordEncoder.encode("academy"))
//                .roles("USER", "ADMIN")
//                .and()
//                .withUser("devdojo2")
//                .password(passwordEncoder.encode("academy"))
//                .roles("USER");
//
//        auth.userDetailsService(devDojoUserDetailsService)
//                .passwordEncoder(passwordEncoder);
//    }

}

