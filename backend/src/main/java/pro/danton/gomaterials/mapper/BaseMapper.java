package pro.danton.gomaterials.mapper;

/**
 * Generic mapper interface for converting between DTOs and entities
 * @param <D> DTO type
 * @param <E> Entity type
 */
public interface BaseMapper<D, E> {
    
    /**
     * Convert entity to DTO
     */
    D toDto(E entity);
    
    /**
     * Convert DTO to entity
     */
    E toEntity(D dto);
}
