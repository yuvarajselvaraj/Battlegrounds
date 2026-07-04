package com.turf.battlegrounds.access;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessRoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public AccessRoleService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public void syncConfiguredRolesAndPermissions() {
        for (AccessDefaults.RoleDefinition roleDefinition : AccessDefaults.ROLES) {
            syncRoleWithPermissions(roleDefinition);
        }
    }

    public Role getUserRole() {
        return getRequiredRole(AccessDefaults.USER_ROLE);
    }

    public Role getAdminRole() {
        return getRequiredRole(AccessDefaults.ADMIN_ROLE);
    }

    public Role getSuperAdminRole() {
        return getRequiredRole(AccessDefaults.SUPER_ADMIN_ROLE);
    }

    private Role getRequiredRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException("Required role is missing: " + name));
    }

    private void syncRoleWithPermissions(AccessDefaults.RoleDefinition roleDefinition) {
        Role role = roleRepository.findByName(roleDefinition.name())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleDefinition.name());
                    return roleRepository.save(newRole);
                });
        role.setDescription(roleDefinition.description());

        for (String permissionName : roleDefinition.permissions()) {
            Permission permission = getOrCreatePermission(permissionName);
            role.addPermission(permission);
        }

        roleRepository.save(role);
    }

    private Permission getOrCreatePermission(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseGet(() -> {
                    Permission newPermission = new Permission();
                    newPermission.setName(name);
                    return permissionRepository.save(newPermission);
                });
        permission.setDescription("Permission: " + name);
        return permissionRepository.save(permission);
    }
}
