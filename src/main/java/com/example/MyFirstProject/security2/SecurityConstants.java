package com.example.MyFirstProject.security2;

public class SecurityConstants {
    public static String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_USER = "/users/registration";
    public static final String SIGN_UP_ADMIN = "/admin/registration";
    public static final String SIGN_IN_URL = "/users/login";

}