package com.turf.battlegrounds.access;

import java.util.List;

public final class AccessDefaults {
    // Define role name constants exactly as they are saved in your database
    public static final String USER_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String SUPER_ADMIN_ROLE = "ROLE_SUPER_ADMIN";

    public static final List<RoleDefinition> ROLES = List.of(
            new RoleDefinition(
                    USER_ROLE,
                    "Default authenticated user",
                    List.of(
                            "profile:read",
                            "profile:update"
                    )
            ),
            new RoleDefinition(
                    ADMIN_ROLE,
                    "Application administrator",
                    List.of(
                            "profile:read",
                            "profile:update",
                            "user:read",
                            "user:write",
                            "role:read",
                            "permission:read"
                    )
            ),
            new RoleDefinition(
                    SUPER_ADMIN_ROLE,
                    "Application super administrator",
                    List.of(
                            "profile:read",
                            "profile:update",
                            "user:read",
                            "user:write",
                            "role:read",
                            "role:write",
                            "permission:read",
                            "permission:write",
                            "system_settings:read",
                            "system_settings:write"
                    )
            )
    );


    private AccessDefaults() {
    }

    public record RoleDefinition(
            String name,
            String description,
            List<String> permissions
    ) {
    }
}
