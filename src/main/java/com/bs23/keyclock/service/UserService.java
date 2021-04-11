package com.bs23.keyclock.service;

import com.bs23.keyclock.request.PasswordReset;
import com.bs23.keyclock.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
@Service
@Slf4j
public class UserService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    public UserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public List<UserRepresentation> findAll() {
        return keycloak
                .realm(realm)
                .users()
                .list();
    }

    public List<UserRepresentation> findByUsername(String username) {
        return keycloak
                .realm(realm)
                .users()
                .search(username);
    }

    public UserRepresentation findById(String id) {
        return keycloak
                .realm(realm)
                .users()
                .get(id)
                .toRepresentation();
    }

    public void assignToGroup(String userId, String groupId) {
        keycloak
                .realm(realm)
                .users()
                .get(userId)
                .joinGroup(groupId);
    }

    public void assignRole(String userId, RoleRepresentation roleRepresentation) {
        keycloak
                .realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));
    }

    public Response create(UserRequest request) {
        CredentialRepresentation password = preparePasswordRepresentation(request.getPassword());
        UserRepresentation user = prepareUserRepresentation(request, password);
        return keycloak
                .realm(realm)
                .users()
                .create(user);
    }

    private CredentialRepresentation preparePasswordRepresentation(String password) {
        CredentialRepresentation cR = new CredentialRepresentation();
        cR.setTemporary(false);
        cR.setType(CredentialRepresentation.PASSWORD);
        cR.setValue(password);
        return cR;
    }

    private UserRepresentation prepareUserRepresentation(UserRequest request, CredentialRepresentation cR) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setCredentials(List.of(cR));
        newUser.setEnabled(true);
        return newUser;
    }

    public Response delete(String id) {
        return keycloak.realm(realm).users().delete(id);
    }

    public boolean resetPassword(String userId, PasswordReset passwordReset) {
        try {
            UserResource ur = keycloak.realm(realm).users().get(userId);
            CredentialRepresentation cr = new CredentialRepresentation();
            cr.setType(CredentialRepresentation.PASSWORD);
            cr.setValue(passwordReset.getValue());
            cr.setTemporary(passwordReset.getTemporary());
            ur.resetPassword(cr);
            return true;
        } catch (Exception e) {
            System.out.println("Password couldn't reset");
        }
        return false;
    }

    public void enableOrDisableUser(String id, UserRequest userRequest) {
        UserResource userResource = keycloak.realm(realm).users().get(id);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(userRequest.getEnabled());
        userResource.update(user);
    }

    public void update(String id, UserRequest request) {
        UserResource userResource = keycloak.realm(realm).users().get(id);
        UserRepresentation user = userResource.toRepresentation();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userResource.update(user);
    }

    public void addOrUpdateAttributes(String id, Map<String, List<String>> attrs) {
        UserResource userResource = keycloak.realm(realm).users().get(id);
        UserRepresentation user = userResource.toRepresentation();
        user.setAttributes(attrs);
        userResource.update(user);
    }

    public void removeAttr(String id, String name) {
        UserResource userResource = keycloak.realm(realm).users().get(id);
        UserRepresentation user = userResource.toRepresentation();
        Map<String, List<String>> attrs = user.getAttributes();
        attrs.remove(name);
        user.setAttributes(attrs);
        userResource.update(user);
    }
}
