package com.turf.battlegrounds.user;

import com.turf.battlegrounds.access.AccessRoleService;
import com.turf.battlegrounds.access.Role;
import com.turf.battlegrounds.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccessRoleService accessRoleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            AccessRoleService accessRoleService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.accessRoleService = accessRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto getUser(Long id)
    {
       User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found with id: " + id));
       return UserMapper.toDto(user);
    }

    public List<UserResponseDto> getAllUsers()
    {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDTO userRequestDTO)
    {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone_no(userRequestDTO.getPhone_no());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.addRole(getDefaultUserRole());
        return UserMapper.toDto(userRepository.save(user));
    }

    private Role getDefaultUserRole() {
        return accessRoleService.getUserRole();
    }
}
