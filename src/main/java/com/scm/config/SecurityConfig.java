package com.scm.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.SecurityCustomUserDetailService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService securityCustomUserDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler failureHandler;

    // user create and login with in memory service
    // @Bean
    // public UserDetailsService userDetailsService(){

    //     UserDetails user1=User
    //     .withDefaultPasswordEncoder()
    //     .username("admin")
    //     .password("admin123")
    //     .roles("ADMIN","User")
    //     .build();

    //     var inMemoryUserDetailsManager=new InMemoryUserDetailsManager(user1);
    //     return inMemoryUserDetailsManager;
    // }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // configuration of authentication proivider spring security
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(authorize->{
            // authorize.requestMatchers("/home").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.formLogin(form->{
            form.loginPage("/login");
            form.loginProcessingUrl("/authenticate");
            form.defaultSuccessUrl("/user/dashboard");
            // form.successForwardUrl("/user/dashboard");
            // form.failureForwardUrl("/login?error=true"); 
            form.usernameParameter("email");
            form.passwordParameter("password");

            form.failureHandler(failureHandler);
        });

        //oAuth configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        return httpSecurity.build();
    }
}
