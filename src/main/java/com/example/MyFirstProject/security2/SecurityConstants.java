package com.example.MyFirstProject.security2;

public class SecurityConstants {
//    JWT
    public static String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

//    Field
    public static final String AUTHORITY = "role";
    public static final String ENABLED = "enabled";
    public static final String EMAIL = "email";

//    URL
    public static final String SIGN_UP_USER = "/users/registration";
    public static final String SIGN_UP_ADMIN = "/admin/registration";
    public static final String SIGN_IN_URL = "/users/login";
    public static final String ACTIVATE_URL = "/users/activate";
    public static final String RESET_PASSWORD = "/users/resetPassword";
    public static final String CHANGE_PASSWORD = "/users/changePassword";

}