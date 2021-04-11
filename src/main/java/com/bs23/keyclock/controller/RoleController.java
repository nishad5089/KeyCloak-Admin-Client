package com.bs23.keyclock.controller;

import com.bs23.keyclock.service.RoleService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {
    public final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleRepresentation>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping
    public void createRole(@RequestParam String name) {
        roleService.create(name);
    }
}
