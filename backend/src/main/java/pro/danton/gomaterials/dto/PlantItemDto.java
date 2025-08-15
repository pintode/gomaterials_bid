package pro.danton.gomaterials.dto;

import lombok.Data;

@Data
public class PlantItemDto {
    private Long id;
    private String name;
    private String grade;
    private Long quantity;
    private String unit;
}
