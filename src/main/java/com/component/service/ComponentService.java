package com.component.service;

import com.component.dto.ComponentDto;
import com.component.mapper.ComponentMapper;
import com.component.model.ComponentEntity;
import com.component.repository.ComponentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentService {

    private final ComponentRepository repo;
    private final ComponentMapper mapper;

    public ComponentService(ComponentRepository repo, ComponentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<ComponentDto> getAllForUser(Long userId) {
        List<ComponentEntity> components = repo.findByOwnerUserId(userId);
        return mapper.toDto(components);
    }

    @Transactional
    public Optional<ComponentDto> createComponent(ComponentDto dto, Long ownerUserId) {
        dto.setOwnerUserId(ownerUserId);
        ComponentEntity component = mapper.toEntity(dto);
        ComponentEntity componentSaved = repo.save(component);

        return Optional.of(mapper.toDto(componentSaved));
    }

    @Transactional
    public Optional<ComponentDto> updateComponent(Long componentId, Long ownerUserId, ComponentDto component) {
        if (repo.findByOwnerUserIdAndId(ownerUserId, componentId).isEmpty())
            return Optional.empty();

        return repo.findByOwnerUserIdAndId(ownerUserId, componentId)
                .map(entity -> {
                    component.setId(componentId);
                    component.setOwnerUserId(ownerUserId);

                    return mapper.toDto(repo.save(mapper.toEntity(component)));
                });
    }

    public Optional<ComponentDto> getComponentById(Long ownerUserId, Long componentId) {
        return repo.findById(componentId)
                .filter(entity -> entity.getOwnerUserId().equals(ownerUserId))
                .map(mapper::toDto);
    }

    @Transactional
    public boolean deleteComponentById(Long ownerUserId, Long componentId) {
        Optional<ComponentEntity> component = repo.findById(componentId);
        if (component.isEmpty() || !component.get().getOwnerUserId().equals(ownerUserId)) {
            return false;
        }
        repo.delete(component.get());
        return true;
    }
}
