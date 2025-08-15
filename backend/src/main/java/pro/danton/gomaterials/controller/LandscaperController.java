package pro.danton.gomaterials.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import pro.danton.gomaterials.dto.LandscaperDto;
import pro.danton.gomaterials.dto.CreateLandscaperDto;
import pro.danton.gomaterials.model.Landscaper;
import pro.danton.gomaterials.service.LandscaperService;
import pro.danton.gomaterials.mapper.LandscaperMapper;

@Validated
@Tag(name = "Landscapers")
@RestController
@RequestMapping("/api/v1/landscapers")
public class LandscaperController {
    @Autowired
    private LandscaperService landscaperService;
    @Autowired
    private LandscaperMapper landscaperMapper;

    @Operation(summary = "Create landscaper")
    @PostMapping
    public ResponseEntity<LandscaperDto> create(
            @Parameter(required = true) @Valid @RequestBody CreateLandscaperDto createLandscaperDto) {
        Landscaper landscaper = landscaperMapper.toEntity(createLandscaperDto);
        
        Landscaper created = landscaperService.create(landscaper);
        return ResponseEntity.status(HttpStatus.CREATED).body(landscaperMapper.toDto(created));
    }

    @Operation(summary = "Get landscaper by id")
    @GetMapping("/{id}")
    public ResponseEntity<LandscaperDto> getById(
            @Parameter(required = true) @PathVariable Long id) {
        return landscaperService.findById(id)
                .map(landscaperMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all landscapers")
    @GetMapping
    public List<LandscaperDto> getAll() {
        return landscaperService.findAll().stream().map(landscaperMapper::toDto).collect(Collectors.toList());
    }
}
