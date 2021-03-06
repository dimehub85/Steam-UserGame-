package com.dimemtl.Model;

import io.javalin.core.security.Role;

public enum UserRole implements Role {
    ADMIN,
    COMMON;
}
