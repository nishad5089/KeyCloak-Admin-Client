package com.bs23.keyclock.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
@Service
public class RoleService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    public RoleService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void create(String name) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(name);
        keycloak.realm(realm)
                .roles()
                .create(role);
    }

    public List<RoleRepresentation> findAll() {
        return keycloak
                .realm(realm)
                .roles()
                .list();
    }

    public RoleRepresentation findByName(String roleName) {
        return keycloak
                .realm(realm)
                .roles()
                .get(roleName)
                .toRepresentation();
    }

    public RoleRepresentation findById(String id) {
        return keycloak.realm(realm).rolesById().getRole(id);
    }

    public void deleteById(String id) {
        keycloak.realm(realm).rolesById().deleteRole(id);
    }

    public void deleteByName(String name) {
        keycloak.realm(realm).roles().deleteRole(name);
    }

}
