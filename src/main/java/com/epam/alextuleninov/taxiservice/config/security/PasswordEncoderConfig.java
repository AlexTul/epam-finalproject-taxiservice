package com.epam.alextuleninov.taxiservice.config.security;

/**
 * To customize EncryptionPasswordEncoder.
 */
public class PasswordEncoderConfig {

    public static EncryptionPasswordEncoder passwordEncoder() {
        return new EncryptionPasswordEncoder("SHA-1", "utf-8");
    }
}
