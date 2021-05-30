package com.javi.uned.pfgbackend.config;

import com.javi.uned.pfgbackend.beans.Sheet;
import com.javi.uned.pfgbackend.beans.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class BeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope("prototype")
    public Sheet sheet(User user){
        Sheet sheet = new Sheet();
        sheet.setOwnerId(1L);
        sheet.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return sheet;
    }
}
