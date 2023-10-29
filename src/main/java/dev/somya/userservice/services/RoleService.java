package dev.somya.userservice.services;

import dev.somya.userservice.models.Role;
import dev.somya.userservice.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name){
        Role role = new Role();
        role.setRole(name);

        return roleRepository.save(role);
    }

}
