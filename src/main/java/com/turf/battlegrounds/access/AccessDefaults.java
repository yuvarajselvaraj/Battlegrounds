package com.turf.battlegrounds.access;

import java.util.List;

public final class AccessDefaults {
    public static final String USER_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String SUPER_ADMIN_ROLE = "ROLE_SUPER_ADMIN";

    public static final List<RoleDefinition> ROLES = List.of(
            new RoleDefinition(
                    USER_ROLE,
                    "Default authenticated user",
                    List.of(
                            "PROFILE_READ",
                            "PROFILE_UPDATE"
                    )
            ),
            new RoleDefinition(
                    ADMIN_ROLE,
                    "Application administrator",
                    List.of(
                            "USER_READ",
                            "USER_WRITE",
                            "ROLE_READ",
                            "PERMISSION_READ"
                    )
            ),
            new RoleDefinition(
                    SUPER_ADMIN_ROLE,
                    "Application super administrator",
                    List.of(
                            "USER_READ",
                            "USER_WRITE",
                            "ROLE_READ",
                            "ROLE_WRITE",
                            "PERMISSION_READ",
                            "PERMISSION_WRITE",
                            "SYSTEM_SETTINGS_READ",
                            "SYSTEM_SETTINGS_WRITE"
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
