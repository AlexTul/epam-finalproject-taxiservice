package com.epam.alextuleninov.taxiservice.config.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EncryptionPasswordEncoderTest {

    private static final String password = "QAsQaqGMoG1K5hQCCVFd";
    private static final String wrongPassword = "QAsQaqGMoG1K5hQCCVFd";

    @Test
    void testPasswordHashing() {
        var encryptPassword = PasswordEncoderConfig
                .passwordEncoder()
                .encrypt(password);

        assertNotEquals(wrongPassword, encryptPassword);
    }

    @Test
    void testVerifying() {
        var encryptPassword = PasswordEncoderConfig
                .passwordEncoder()
                .encrypt(password);

        assertEquals("9a497b0374e8e798f44291ad4a2fe4aad20f11bb", encryptPassword);
    }
}
