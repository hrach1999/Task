package com.sflpro.chat.config;

import com.sflpro.chat.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin().
                loginPage("/login").usernameParameter("email").passwordParameter("password").
                defaultSuccessUrl("/loginSuccess").
                failureUrl("/login?error=true").permitAll().and().
                logout().logoutSuccessUrl("/login").and()
                .authorizeRequests().
                antMatchers("/").permitAll().
                antMatchers("/removeUser").hasAnyAuthority("ADMIN").
                antMatchers("/manageUsers").hasAnyAuthority("ADMIN").
                antMatchers("/admin").hasAnyAuthority("ADMIN").
                antMatchers("/user").hasAnyAuthority("USER");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}

