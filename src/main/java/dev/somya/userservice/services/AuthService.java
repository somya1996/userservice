package dev.somya.userservice.services;

import dev.somya.userservice.dtos.UserDto;
import dev.somya.userservice.models.Session;
import dev.somya.userservice.models.SessionStatus;
import dev.somya.userservice.models.User;
import dev.somya.userservice.repositories.RoleRepository;
import dev.somya.userservice.repositories.SessionRepository;
import dev.somya.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Map;
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
         String token = RandomStringUtils.randomAlphanumeric(30);

         Session session = new Session();
         session.setSessionStatus(SessionStatus.ACTIVE);
         session.setToken(token);
         session.setUser(user);
         sessionRepository.save(session);

         Map<String , String> headers = new HashMap<>();
         //headers.put(HttpHeaders.SET_COOKIE , token);

         UserDto userDto = UserDto.from(user);
         ResponseEntity<UserDto> response = new ResponseEntity<>(userDto , null , HttpStatus.OK);
         response.getHeaders().add(HttpHeaders.SET_COOKIE , token);
         return response;
     }

     public ResponseEntity<Void> logout(String token , Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token, userId);
        if(sessionOptional.isEmpty()){
            return null;
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
     }

     public UserDto signUp(String email , String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
     }

     public SessionStatus validate(String token , Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token, userId);
        if(sessionOptional.isEmpty()){
            return SessionStatus.ENDED;
        }
        Session session = sessionOptional.get();
        if(!session.getSessionStatus().equals(SessionStatus.ACTIVE)){
            return SessionStatus.ENDED;
        }
        return SessionStatus.ACTIVE;
     }
}
