package pro.danton.gomaterials.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import pro.danton.gomaterials.model.BidRequestStatus;

@Data
public class BidRequestDto {
    private Long id;
    private LocalDateTime createdAt;
    private String projectName;
    private LocalDate requiredBy;
    private LandscaperDto landscaper;
    private List<PlantItemDto> plantItems;
    private BidRequestStatus status;
    private List<BidResponseDto> bidResponses;
}
