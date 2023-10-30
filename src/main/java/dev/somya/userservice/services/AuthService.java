package dev.somya.userservice.services;

import dev.somya.userservice.dtos.UserDto;
import dev.somya.userservice.models.Session;
import dev.somya.userservice.models.SessionStatus;
import dev.somya.userservice.models.User;
import dev.somya.userservice.repositories.RoleRepository;
import dev.somya.userservice.repositories.SessionRepository;
import dev.somya.userservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.SecretKey;
import javax.swing.text.html.Option;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository , BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
     public ResponseEntity<UserDto> login(String email , String password){
         Optional<User> userOptional = userRepository.findByEmail(email);
         if(userOptional.isEmpty()){
             return null;
         }

         /**    Using BCrypt to encode the password */
         User user = userOptional.get();
         if(!bCryptPasswordEncoder.matches(password , user.getPassword())){
             throw new RuntimeException("Wrong username password");
//             return null;
         }

         /**    Creating jjwt   */
//       String token = RandomStringUtils.randomAlphanumeric(30);
         // Create a test key suitable for the desired HMAC-SHA algorithm:
         MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
         SecretKey key = alg.key().build();

//         String message = "{\n" +
//                 "  \"email\": \"naman@gmail.com\",\n" +
//                 "  \"roles\": [\n" +
//                 "     \"mentor\" ,\n" +
//                 "     \"ta\"\n" +
//                 "     ],\n" +
//                 "  \"expirationDate\": \"23October2023\"\n" +
//                 "}";
//         byte[] content = message.getBytes(StandardCharsets.UTF_8);
//
//// Create the compact JWS:
//         String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();
//
//// Parse the compact JWS:
////         content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();
//
//         assert message.equals(new String(content, StandardCharsets.UTF_8));

         /**Creating compact token which will be easy to understand*/
         Map<String , Object> jsonFromJwt = new HashMap<>();
         jsonFromJwt.put("email" , user.getEmail());
         jsonFromJwt.put("role" , user.getRoles());
         jsonFromJwt.put("createdAt" , new Date());
         jsonFromJwt.put("expiryAt" , new Date(LocalDate.now().plusDays(3).toEpochDay()));

         String token = Jwts.builder()
                 .claims(jsonFromJwt)
                 .signWith(key , alg)
                 .compact();

         Session session = new Session();
         session.setSessionStatus(SessionStatus.ACTIVE);
//       session.setToken(jws);
         session.setToken(token);
         session.setUser(user);
         sessionRepository.save(session);

         /** USED TO SET HEADERS */
//       Map<String , String> headers = new HashMap<>();
//       headers.put(HttpHeaders.SET_COOKIE , token);
         UserDto userDto = UserDto.from(user);
         MultiValueMapAdapter<String , String> headers = new MultiValueMapAdapter<>(new HashMap<>());
//       headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + jws);
         headers.add(HttpHeaders.SET_COOKIE , "auth-token:" + token);

         /**RESPONSE ENTITY Implementation */
         ResponseEntity<UserDto> response = new ResponseEntity<>(userDto , headers , HttpStatus.OK);
//       response.getHeaders().add(HttpHeaders.SET_COOKIE , token);
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
         user.setPassword(bCryptPasswordEncoder.encode(password));

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
//Before using setting JJwt
//auth-token%3AeyJjdHkiOiJ0ZXh0L3BsYWluIiwiYWxnIjoiSFMyNTYifQ.ewogICJlbWFpbCI6ICJuYW1hbkBnbWFpbC5jb20iLAogICJyb2xlcyI6IFsKICAgICAibWVudG9yIiAsCiAgICAgInRhIgogICAgIF0sCiAgImV4cGlyYXRpb25EYXRlIjogIjIzT2N0b2JlcjIwMjMiCn0.yMCFy7FyBDwGLPdPrTJkCvoEZF6-7FnO40cgBtea8Us
// Using Map and build jjwt
//auth-token%3AeyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVkQXQiOjE2OTg2ODIwNTQ0NjYsInJvbGUiOltdLCJleHBpcnlBdCI6MTk2NjMsImVtYWlsIjoibmFtYW5Ac2NhbGVyLmNvbSJ9.LYRTL5VN00tHdOB0AgWo4XUfBnZ8lXQy7pvb3rzbRXU