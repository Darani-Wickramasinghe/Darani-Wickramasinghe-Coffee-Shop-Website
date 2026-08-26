package com.coffeeshop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey secretKey;
    private final long jwtExpirationMs;

    public JwtTokenProvider(@Value("${jwt.expiration-ms:86400000}") long jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
        this.secretKey = resolveSecretKey();
    }

    /**
     * Resolves the JWT signing key using a multi-tiered strategy:
     * 1. Environment variable JWT_SECRET
     * 2. File jwt_secret.txt in working directory
     * 3. Ephemeral random secret (with warning log)
     */
    private SecretKey resolveSecretKey() {
        // Tier 1: Environment variable
        String envSecret = System.getenv("JWT_SECRET");
        if (envSecret != null && !envSecret.isBlank()) {
            logger.info("JWT secret loaded from environment variable");
            return Keys.hmacShaKeyFor(Base64.getDecoder().decode(envSecret));
        }

        // Tier 2: File-based secret
        Path secretFile = Path.of("jwt_secret.txt");
        if (Files.exists(secretFile)) {
            try {
                String fileSecret = Files.readString(secretFile).trim();
                if (!fileSecret.isBlank()) {
                    logger.info("JWT secret loaded from file: jwt_secret.txt");
                    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(fileSecret));
                }
            } catch (IOException e) {
                logger.warn("Failed to read jwt_secret.txt, falling back to ephemeral secret");
            }
        }

        // Tier 3: Ephemeral random secret
        logger.warn("SECURITY WARNING: Generating ephemeral JWT secret. "
                + "This secret is instance-isolated and will not survive restarts. "
                + "Set JWT_SECRET environment variable for production use.");
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[64];
        secureRandom.nextBytes(keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    public String getRoleFromToken(String token) {
        return parseToken(token).getPayload().get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> jws = parseToken(token);

            // Reject tokens with 'none' algorithm
            if ("none".equalsIgnoreCase(jws.getHeader().getAlgorithm())) {
                logger.warn("Rejected token with 'none' algorithm");
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token");
        } catch (SecurityException e) {
            logger.warn("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty");
        }
        return false;
    }

    private Jws<Claims> parseToken(String token) {
        // Hardcoded algorithm verification — only accepts HS256 signed with our key
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}
