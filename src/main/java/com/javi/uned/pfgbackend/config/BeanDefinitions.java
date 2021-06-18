package com.javi.uned.pfgbackend.config;

import com.javi.uned.pfgbackend.adapters.database.user.UserDAOImpl;
import com.javi.uned.pfgbackend.domain.ports.database.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanDefinitions {

    @Bean
    public UserDAO userDAO(UserDAOImpl userDAO) {
        return userDAO;
    }

}
