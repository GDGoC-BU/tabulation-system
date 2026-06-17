package com.gdgocbu.tabulation.backend.utilities;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Named("encodePassword")
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword); // bcrypt
    }
}
