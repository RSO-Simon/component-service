package com.component.auth;

public final class AuthContext {
    private static final ThreadLocal<Long> OWNER = new ThreadLocal<>();

    private AuthContext() { }

    public static void setOwnerUserId(Long id) { OWNER.set(id); }
    public static Long getOwnerUserId() { return OWNER.get(); }
    public static void clear() { OWNER.remove(); }
}
