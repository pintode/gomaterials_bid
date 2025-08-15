package pro.danton.gomaterials.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.danton.gomaterials.dto.BidRequestDto;
import pro.danton.gomaterials.dto.CreateBidRequestDto;
import pro.danton.gomaterials.model.BidRequest;

@Mapper(componentModel = "spring", uses = {LandscaperMapper.class, PlantItemMapper.class, BidResponseMapper.class})
public interface BidRequestMapper extends BaseMapper<BidRequestDto, BidRequest> {
    
    @Override
    BidRequestDto toDto(BidRequest bidRequest);
    
    @Override
    @Mapping(target = "status", ignore = true)
    BidRequest toEntity(BidRequestDto bidRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "landscaper", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "bidResponses", ignore = true)
    BidRequest toEntity(CreateBidRequestDto createBidRequestDto);
}
