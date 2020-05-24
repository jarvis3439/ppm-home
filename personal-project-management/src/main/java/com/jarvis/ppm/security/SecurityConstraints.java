package com.jarvis.ppm.security;

public class SecurityConstraints {
	
	public static final String SIGN_UP_URL = "/user/**";
	public static final String SECRET_KEY = "SecretKeyToGenerateJWTs";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 300_000; // 30 seconds
}
