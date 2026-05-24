package com.turf.battlegrounds.user;

import com.turf.battlegrounds.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserDetail(@PathVariable Long id)
    {
        UserResponseDto user = userService.getUser(id);
        ApiResponse<UserResponseDto> userResponse = new ApiResponse<>(200, "success", "User Detail fetched successfully", user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers()
    {
        List<UserResponseDto> users= userService.getAllUsers();
        ApiResponse<List<UserResponseDto>> user = new ApiResponse<>(200, "success", "User Details Fetched Successfully", users);
        return ResponseEntity.ok(user);
    }
}
