package dev.somya.userservice.services;

import dev.somya.userservice.dtos.UserDto;
import dev.somya.userservice.models.User;
import dev.somya.userservice.repositories.RoleRepository;
import dev.somya.userservice.repositories.SessionRepository;
import dev.somya.userservice.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }
     public ResponseEntity<UserDto> login(String email , String password){
         Optional<User> userOptional = userRepository.findByEmail(email);
         if(userOptional.isEmpty()){
             return null;
         }

            User user = userOptional.get();
            if(!user.getPassword().equals(password)){
                return null;
            }
            return ResponseEntity.ok(UserDto.from(user));
     }
}
