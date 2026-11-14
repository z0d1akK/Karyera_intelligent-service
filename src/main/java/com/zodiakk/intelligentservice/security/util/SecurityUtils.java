package com.zodiakk.intelligentservice.security.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean keysMatch(String expectedKey, String providedKey) {
        if (expectedKey == null || expectedKey.isBlank() || providedKey == null) {
            return false;
        }

        return MessageDigest.isEqual(
                expectedKey.getBytes(StandardCharsets.UTF_8),
                providedKey.getBytes(StandardCharsets.UTF_8)
        );
    }
}
