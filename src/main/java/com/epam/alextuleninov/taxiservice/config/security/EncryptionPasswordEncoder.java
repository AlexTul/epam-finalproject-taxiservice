package com.epam.alextuleninov.taxiservice.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for generation string for password.
 */
public class EncryptionPasswordEncoder {

    private static final Logger log = LoggerFactory.getLogger(EncryptionPasswordEncoder.class);

    private final String algorithm;
    private final String charset;

    public EncryptionPasswordEncoder(String algorithm, String charset) {
        this.algorithm = algorithm;
        this.charset = charset;
    }

    /**
     * Generate string for password.
     *
     * @param password      user`s password
     * @return              string with encrypted user`s password
     */
    public String encrypt(String password) {
        MessageDigest messageDigest;
        byte[] bytesEncoded = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(password.getBytes(charset));
            bytesEncoded = messageDigest.digest();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("Hashing password error", e);
        }
        assert bytesEncoded != null;
        BigInteger bigInteger = new BigInteger(1, bytesEncoded);
        return bigInteger.toString(16);
    }
}
