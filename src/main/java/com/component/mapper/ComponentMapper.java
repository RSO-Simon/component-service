package com.component.mapper;


import com.component.dto.ComponentDto;
import com.component.model.ComponentEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ComponentMapper {

    ComponentDto toDto(ComponentEntity entity);
    ComponentEntity toEntity(ComponentDto dto);
    List<ComponentDto> toDto(List<ComponentEntity> entities);
}
