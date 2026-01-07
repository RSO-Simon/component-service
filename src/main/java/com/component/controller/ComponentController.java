package com.component.controller;

import com.component.dto.ComponentDto;
import com.component.service.ComponentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam Long ownerUserId
    ) {
        return componentService.getAllForUser(ownerUserId);
    }

    @PostMapping
    public ResponseEntity<ComponentDto> create(
            @RequestBody ComponentDto componentDto,
            @RequestParam Long ownerUserId
    ) {
        return componentService.createComponent(componentDto, ownerUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{componentId}")
    public ResponseEntity<ComponentDto> update(@PathVariable Long componentId,
                                               @RequestParam Long ownerUserId,
                                               @RequestBody ComponentDto componentDto
    ) {
        return componentService.updateComponent(componentId, ownerUserId, componentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{componentId}")
    public ResponseEntity<ComponentDto> getById(@PathVariable Long componentId,
                                                @RequestParam Long ownerUserId
    ) {
        return componentService.getComponentById(ownerUserId, componentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{componentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long componentId,
            @RequestParam Long ownerUserId
    ) {
        if (componentService.deleteComponentById(ownerUserId, componentId))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

}
