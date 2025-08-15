package pro.danton.gomaterials.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pro.danton.gomaterials.dto.SupplierDto;
import pro.danton.gomaterials.dto.CreateSupplierDto;
import pro.danton.gomaterials.model.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends BaseMapper<SupplierDto, Supplier> {
    
    @Override
    SupplierDto toDto(Supplier supplier);
    
    @Override
    @Mapping(target = "bidResponses", ignore = true)
    Supplier toEntity(SupplierDto supplierDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bidResponses", ignore = true)
    Supplier toEntity(CreateSupplierDto createSupplierDto);
}
