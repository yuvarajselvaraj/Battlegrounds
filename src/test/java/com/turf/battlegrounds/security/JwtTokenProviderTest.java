package com.turf.battlegrounds.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private static final String SECRET = "test-secret-that-is-at-least-32-bytes-long";

    private final JwtTokenProvider provider = new JwtTokenProvider(SECRET, 900_000, 604_800_000);
    private final UserDetails user = User.withUsername("alice").password("x")
            .authorities("ROLE_USER", "profile:read").build();

    @Test
    void accessTokenCarriesSubjectAndAuthorities() {
        Jwt jwt = provider.accessTokenDecoder().decode(provider.generateAccessToken(user));
        assertEquals("alice", jwt.getSubject());
        assertEquals(List.of("ROLE_USER", "profile:read"), jwt.getClaimAsStringList(JwtTokenProvider.AUTHORITIES_CLAIM));
    }

    @Test
    void accessTokenIsNotAcceptedAsRefreshToken() {
        assertFalse(provider.validateRefreshToken(provider.generateAccessToken(user)));
    }

    @Test
    void refreshTokenIsValidOnlyAsRefreshToken() {
        String refresh = provider.generateRefreshToken(user);
        assertTrue(provider.validateRefreshToken(refresh));
        assertEquals("alice", provider.getUsernameFromRefreshToken(refresh));
        assertThrows(JwtException.class, () -> provider.accessTokenDecoder().decode(refresh));
    }

    @Test
    void tokenSignedWithDifferentSecretIsRejected() {
        JwtTokenProvider other = new JwtTokenProvider("another-secret-that-is-also-32-bytes-long", 900_000, 604_800_000);
        String access = other.generateAccessToken(user);
        assertThrows(JwtException.class, () -> provider.accessTokenDecoder().decode(access));
    }

    @Test
    void expiredAccessTokenIsRejected() {
        // Signed with the right secret and type, but expired beyond the default 60s clock skew
        SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Instant issuedAt = Instant.now().minus(Duration.ofMinutes(10));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("alice")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(Duration.ofMinutes(5)))
                .claim("type", "access")
                .build();
        String access = new NimbusJwtEncoder(new ImmutableSecret<>(key))
                .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
        assertThrows(JwtException.class, () -> provider.accessTokenDecoder().decode(access));
    }

    @Test
    void shortSecretIsRejectedAtStartup() {
        assertThrows(IllegalArgumentException.class, () -> new JwtTokenProvider("too-short", 900_000, 604_800_000));
    }
}
