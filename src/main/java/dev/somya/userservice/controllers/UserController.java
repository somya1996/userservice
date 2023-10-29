package dev.somya.userservice.controllers;

import dev.somya.userservice.dtos.SetUserRolesRequestDto;
import dev.somya.userservice.dtos.UserDto;
import dev.somya.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserDetails(userId);
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDto> setUserRoles(@PathVariable("id") Long userId , @RequestBody SetUserRolesRequestDto request){
        UserDto userDto = userService.setUserRoles(userId , request.getRoleIds());
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }


}
