package com.sflpro.chat.util;

import java.security.SecureRandom;

public class SecurePasswordGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String DICTIONARY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*_=+-/";
    private static final int PASSWORD_LENGTH = 12;

    private SecurePasswordGenerator() {
    }

    public static String generatePassword() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(DICTIONARY.length());
            result.append(DICTIONARY.charAt(index));
        }
        return result.toString();
    }
}
