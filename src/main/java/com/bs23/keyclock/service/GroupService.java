package com.bs23.keyclock.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
@Service
public class GroupService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    public GroupService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void create(String name) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(name);
        keycloak.realm(realm)
                .groups()
                .add(group);
    }

    public List<GroupRepresentation> findAll() {
        return keycloak
                .realm(realm)
                .groups()
                .groups();
    }

    public GroupRepresentation findById(String id) {
        return keycloak
                .realm(realm)
                .groups()
                .group(id)
                .toRepresentation();
    }
}
