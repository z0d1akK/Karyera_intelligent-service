package com.zodiakk.intelligentservice.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String INTERNAL_SERVICE_AUTHORITY = ROLE_PREFIX + "INTERNAL_SERVICE";
}
