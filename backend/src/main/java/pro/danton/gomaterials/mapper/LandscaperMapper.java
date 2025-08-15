package pro.danton.gomaterials.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pro.danton.gomaterials.dto.LandscaperDto;
import pro.danton.gomaterials.dto.CreateLandscaperDto;
import pro.danton.gomaterials.model.Landscaper;

@Mapper(componentModel = "spring")
public interface LandscaperMapper extends BaseMapper<LandscaperDto, Landscaper> {
    
    @Override
    LandscaperDto toDto(Landscaper landscaper);
    
    @Override
    @Mapping(target = "bidRequests", ignore = true)
    Landscaper toEntity(LandscaperDto landscaperDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bidRequests", ignore = true)
    Landscaper toEntity(CreateLandscaperDto createLandscaperDto);
}
