package dev.somya.userservice.dtos;

import dev.somya.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private Long phone;
}
