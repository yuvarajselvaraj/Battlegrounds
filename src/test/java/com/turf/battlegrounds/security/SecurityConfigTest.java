package com.turf.battlegrounds.security;

import com.turf.battlegrounds.home.HomeController;
import com.turf.battlegrounds.user.UserController;
import com.turf.battlegrounds.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {HomeController.class, UserController.class})
@Import({SecurityConfig.class, JwtTokenProvider.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
@TestPropertySource(properties = "jwt.secret=test-secret-that-is-at-least-32-bytes-long")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private UserService userService;

    // @EnableJpaAuditing on the application class needs this in a web slice test
    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private final UserDetails user = User.withUsername("alice").password("x").authorities("ROLE_USER").build();

    @Test
    void rootIsPublic() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void invalidTokenIsIgnoredOnPublicPaths() throws Exception {
        mockMvc.perform(get("/").header("Authorization", "Bearer not-a-real-token"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer"));
    }

    @Test
    void protectedEndpointWithAccessTokenSucceeds() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + tokenProvider.generateAccessToken(user)))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointRejectsRefreshToken() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + tokenProvider.generateRefreshToken(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointRejectsGarbageToken() throws Exception {
        mockMvc.perform(get("/api/v1/users").header("Authorization", "Bearer not-a-real-token"))
                .andExpect(status().isUnauthorized());
    }
}
