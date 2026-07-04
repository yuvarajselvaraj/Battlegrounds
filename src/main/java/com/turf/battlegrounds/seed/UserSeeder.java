package com.turf.battlegrounds.seed;

import com.turf.battlegrounds.access.AccessRoleService;
import com.turf.battlegrounds.access.Role;
import com.turf.battlegrounds.user.User;
import com.turf.battlegrounds.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserSeeder implements CommandLineRunner {
    private static final String SUPER_ADMIN_USERNAME = "superadmin";
    private static final String SUPER_ADMIN_EMAIL = "superadmin@battlegrounds.local";
    private static final String SUPER_ADMIN_PHONE_NO = "9999999999";
    private static final String SUPER_ADMIN_PASSWORD = "SuperAdmin@123";

    private final UserRepository userRepository;
    private final AccessRoleService accessRoleService;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            UserRepository userRepository,
            AccessRoleService accessRoleService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.accessRoleService = accessRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        accessRoleService.syncConfiguredRolesAndPermissions();

        if (userRepository.findByUsername(SUPER_ADMIN_USERNAME).isPresent()) {
            return;
        }

        createSuperAdminUser();
    }

    private void createSuperAdminUser() {
        User user = new User();
        user.setUsername(SUPER_ADMIN_USERNAME);
        user.setEmail(SUPER_ADMIN_EMAIL);
        user.setPhone_no(SUPER_ADMIN_PHONE_NO);
        user.setPassword(passwordEncoder.encode(SUPER_ADMIN_PASSWORD));

        Role superAdminRole = accessRoleService.getSuperAdminRole();
        user.addRole(superAdminRole);

        userRepository.save(user);
    }
}
