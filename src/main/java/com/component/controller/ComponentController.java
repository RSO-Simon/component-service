package com.component.controller;

import com.component.auth.AuthContext;
import com.component.dto.ComponentDto;
import com.component.service.ComponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(
        name = "Components",
        description = "Operations for managing component definitions owned by the authenticated user"
)
@RestController
@RequestMapping
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @Operation(
            summary = "List components for current user",
            description = "Returns all component definitions owned by the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of components owned by the authenticated user",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ComponentDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            )
    })
    @GetMapping
    public List<ComponentDto> getAllForUser() {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return componentService.getAllForUser(ownerUserId);
    }

    @Operation(
            summary = "Create a component definition",
            description = "Creates a new component definition for the authenticated user. "
                    + "The ownerUserId is resolved from the JWT and cannot be set by the client."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Component successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComponentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Component could not be created"
            )
    })
    @PostMapping
    public ResponseEntity<ComponentDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Component definition to create. Fields 'id' and 'ownerUserId' are ignored if provided.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ComponentDto.class))
            )
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

    @Operation(
            summary = "Update a component definition",
            description = "Updates an existing component definition owned by the authenticated user. "
                    + "The ownerUserId is resolved from the JWT and cannot be set by the client."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Component successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComponentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Component not found for this user"
            )
    })
    @PutMapping("/{componentId}")
    public ResponseEntity<ComponentDto> update(
            @PathVariable
            @Schema(description = "Component identifier", example = "5")
            Long componentId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated component definition. Fields 'id' and 'ownerUserId' are ignored if provided.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ComponentDto.class))
            )
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

    @Operation(
            summary = "Get a component definition by ID",
            description = "Returns a single component definition by ID if it belongs to the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Component found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComponentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Component not found for this user"
            )
    })
    @GetMapping("/{componentId}")
    public ResponseEntity<ComponentDto> getById(
            @PathVariable
            @Schema(description = "Component identifier", example = "5")
            Long componentId
    ) {
        Long ownerUserId = AuthContext.getOwnerUserId();
        if (ownerUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return componentService.getComponentById(ownerUserId, componentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a component definition",
            description = "Deletes a component definition by ID if it belongs to the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Component successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Component not found for this user"
            )
    })
    @DeleteMapping("/{componentId}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Schema(description = "Component identifier", example = "5")
            Long componentId
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
