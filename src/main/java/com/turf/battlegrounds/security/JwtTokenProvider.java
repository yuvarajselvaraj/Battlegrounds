package com.turf.battlegrounds.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class JwtTokenProvider {

    public static final String AUTHORITIES_CLAIM = "authorities";
    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final int MIN_SECRET_BYTES = 32; // HS256 needs a key of at least 256 bits

    private final JwtEncoder encoder;
    private final JwtDecoder accessTokenDecoder;
    private final JwtDecoder refreshTokenDecoder;
    private final Duration accessTokenValidity;
    private final Duration refreshTokenValidity;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-ms:900000}") long accessTokenValidityMs,
            @Value("${jwt.refresh-token-validity-ms:604800000}") long refreshTokenValidityMs
    ) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_SECRET_BYTES) {
            throw new IllegalArgumentException("jwt.secret must be at least " + MIN_SECRET_BYTES + " bytes");
        }
        SecretKey key = new SecretKeySpec(secretBytes, "HmacSHA256");
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        this.accessTokenDecoder = buildDecoder(key, ACCESS_TOKEN_TYPE);
        this.refreshTokenDecoder = buildDecoder(key, REFRESH_TOKEN_TYPE);
        this.accessTokenValidity = Duration.ofMillis(accessTokenValidityMs);
        this.refreshTokenValidity = Duration.ofMillis(refreshTokenValidityMs);
    }

    // Access tokens carry the user's roles and permissions, so requests are authorized without a DB lookup
    public String generateAccessToken(UserDetails userDetails) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return encode(userDetails.getUsername(), ACCESS_TOKEN_TYPE, accessTokenValidity, authorities);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return encode(userDetails.getUsername(), REFRESH_TOKEN_TYPE, refreshTokenValidity, null);
    }

    /** Decoder used by the resource server; accepts only access tokens. */
    public JwtDecoder accessTokenDecoder() {
        return accessTokenDecoder;
    }

    public boolean validateRefreshToken(String token) {
        try {
            refreshTokenDecoder.decode(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    public String getUsernameFromRefreshToken(String token) {
        return refreshTokenDecoder.decode(token).getSubject();
    }

    private String encode(String subject, String type, Duration validity, List<String> authorities) {
        Instant now = Instant.now();
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(now.plus(validity))
                .claim(TOKEN_TYPE_CLAIM, type);
        if (authorities != null) {
            claims.claim(AUTHORITIES_CLAIM, authorities);
        }
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims.build())).getTokenValue();
    }

    private static JwtDecoder buildDecoder(SecretKey key, String expectedType) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        OAuth2TokenValidator<Jwt> typeValidator = jwt -> expectedType.equals(jwt.getClaimAsString(TOKEN_TYPE_CLAIM))
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Wrong token type", null));
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefault(), typeValidator));
        return decoder;
    }
}
