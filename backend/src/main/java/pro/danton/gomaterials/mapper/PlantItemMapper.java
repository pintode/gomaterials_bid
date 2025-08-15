package pro.danton.gomaterials.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pro.danton.gomaterials.dto.PlantItemDto;
import pro.danton.gomaterials.model.PlantItem;

@Mapper(componentModel = "spring")
public interface PlantItemMapper extends BaseMapper<PlantItemDto, PlantItem> {

    @Override
    PlantItemDto toDto(PlantItem plantItem);

    @Override
    @Mapping(target = "bidRequest", ignore = true)
    PlantItem toEntity(PlantItemDto plantItemDto);
}
