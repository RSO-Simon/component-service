package com.component.controller;

import com.component.auth.AuthContext;
import com.component.dto.ComponentDto;
import com.component.service.ComponentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping
    public List<ComponentDto> getAllForUser(
    ) {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return componentService.getAllForUser(ownerUserId);
    }

    @PostMapping
    public ResponseEntity<ComponentDto> create(
            @RequestBody ComponentDto componentDto
    ) {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return componentService.createComponent(componentDto, ownerUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{componentId}")
    public ResponseEntity<ComponentDto> update(
            @PathVariable Long componentId,
            @RequestBody ComponentDto componentDto
    ) {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return componentService.updateComponent(componentId, ownerUserId, componentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{componentId}")
    public ResponseEntity<ComponentDto> getById(
            @PathVariable Long componentId
    ) {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return componentService.getComponentById(ownerUserId, componentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{componentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long componentId
    ) {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (componentService.deleteComponentById(ownerUserId, componentId))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

}
