package com.bs23.keyclock.controller;

import com.bs23.keyclock.service.GroupService;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    @GetMapping
    public ResponseEntity<List<GroupRepresentation>> findAll() {
        return ResponseEntity.ok(groupService.findAll());
    }

    @PostMapping
    public void createGroup(@RequestParam String name){
        groupService.create(name);
    }
}
