package pro.danton.gomaterials.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pro.danton.gomaterials.dto.BidResponseDto;
import pro.danton.gomaterials.dto.CreateBidResponseDto;
import pro.danton.gomaterials.model.BidResponse;

@Mapper(componentModel = "spring", uses = {SupplierMapper.class})
public interface BidResponseMapper extends BaseMapper<BidResponseDto, BidResponse> {
    
    @Override
    BidResponseDto toDto(BidResponse bidResponse);
    
    @Override
    @Mapping(target = "bidRequest", ignore = true)
    BidResponse toEntity(BidResponseDto bidResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "bidRequest", ignore = true)
    @Mapping(target = "status", ignore = true)
    BidResponse toEntity(CreateBidResponseDto createBidResponseDto);
}
