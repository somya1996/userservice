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
        role.setRole_name(name);
        Role savedroles = roleRepository.save(role);
        return savedroles;
    }

}
