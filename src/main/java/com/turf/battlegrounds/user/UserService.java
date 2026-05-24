package com.turf.battlegrounds.user;

import com.turf.battlegrounds.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class UserService {
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

}
