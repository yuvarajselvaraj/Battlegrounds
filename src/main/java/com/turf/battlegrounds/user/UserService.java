package com.turf.battlegrounds.user;

import com.turf.battlegrounds.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDto getUser(Long id)
    {
       User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found with id: " + id));
       return UserMapper.toDto(user);
    }

    public List<UserResponseDto> getAllUsers()
    {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    public UserResponseDto createUser(UserRequestDTO userRequestDTO)
    {
        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone_no(userRequestDTO.getPhone_no());
        user.setPassword(encoder.encode(userRequestDTO.getPassword()));
        return UserMapper.toDto(userRepository.save(user));
    }

}
