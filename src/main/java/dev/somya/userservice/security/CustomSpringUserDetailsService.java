package dev.somya.userservice.security;

import dev.somya.userservice.models.User;
import dev.somya.userservice.repositories.UserRepository;
import dev.somya.userservice.security.CustomSpringUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomSpringUserDetailsService
        implements UserDetailsService {
    private UserRepository userRepository;

    public CustomSpringUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String naman) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(naman);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User doesn't exist");
        }

        User user = userOptional.get();
        return new CustomSpringUserDetails(user);
    }
}

