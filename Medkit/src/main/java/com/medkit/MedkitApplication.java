package com.medkit;

import com.medkit.session.SessionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
public class MedkitApplication {

    public static void main(String[] args) {
        SessionManager.createInstance();

        SpringApplication.run(MedkitApplication.class, args);
    }

}
