package com.bs23.keyclock.controller;

import com.bs23.keyclock.request.PasswordReset;
import com.bs23.keyclock.request.UserRequest;
import com.bs23.keyclock.service.RoleService;
import com.bs23.keyclock.service.UserService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public List<UserRepresentation> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserRepresentation findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/username/{username}")
    public List<UserRepresentation> findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PostMapping
    public ResponseEntity<URI> create(@RequestBody UserRequest userRequest) {
        Response response = userService.create(userRequest);
        if (response.getStatus() != 201)
            throw new RuntimeException("User was not created");
        return ResponseEntity.created(response.getLocation()).build();
    }

    @PostMapping("/{userId}/group/{groupId}")
    public void assignToGroup(@PathVariable String userId, @PathVariable String groupId) {
        userService.assignToGroup(userId, groupId);
    }

    @PostMapping("/{userId}/role/{roleName}")
    public void assignRole(@PathVariable String userId, @PathVariable String roleName) {
        RoleRepresentation role = roleService.findByName(roleName);
        userService.assignRole(userId, role);
    }

    @DeleteMapping("/{id}")
    public Response deleteUser(@PathVariable String id) {
        return userService.delete(id);
    }

    @PutMapping("reset-pass/{id}")
    public boolean resetPassword(@PathVariable String id, @RequestBody PasswordReset passwordReset) {
        return userService.resetPassword(id, passwordReset);
    }

    @PutMapping("status/{id}")
    public void enableOrDisableUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
        userService.enableOrDisableUser(id, userRequest);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
        userService.update(id, userRequest);
    }

    @PostMapping("/add-attribute/{id}")
    public void addAttribute(@PathVariable String id, @RequestBody Map<String, List<String>> attrs) {
        userService.addOrUpdateAttributes(id, attrs);
    }

    @DeleteMapping("/{userId}/attr-delete/{attrName}")
    public void deleteAttribute(@PathVariable String userId, @PathVariable String attrName) {
        userService.removeAttr(userId, attrName);
    }
}
