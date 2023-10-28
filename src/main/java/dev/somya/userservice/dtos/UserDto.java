package dev.somya.userservice.dtos;

import dev.somya.userservice.models.Role;
import dev.somya.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;
    private Set<Role> roles = new HashSet<>();

    public static UserDto from(User user){
        UserDto userdto = new UserDto();
        userdto.setEmail(user.getEmail());
        userdto.setRoles(user.getRoles());
        return userdto;
    }

}
