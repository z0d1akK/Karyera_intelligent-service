package com.zodiakk.intelligentservice.security;

import com.zodiakk.intelligentservice.security.util.SecurityUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityUtilsTest {

    @Test
    void comparesInternalKeysSafely() {
        assertTrue(SecurityUtils.keysMatch("secret", "secret"));
        assertFalse(SecurityUtils.keysMatch("secret", "other"));
        assertFalse(SecurityUtils.keysMatch("secret", null));
        assertFalse(SecurityUtils.keysMatch(null, "secret"));
        assertFalse(SecurityUtils.keysMatch("  ", "  "));
    }
}
