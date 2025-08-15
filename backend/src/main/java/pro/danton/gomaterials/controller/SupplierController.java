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
import pro.danton.gomaterials.dto.SupplierDto;
import pro.danton.gomaterials.dto.CreateSupplierDto;
import pro.danton.gomaterials.model.Supplier;
import pro.danton.gomaterials.service.SupplierService;
import pro.danton.gomaterials.mapper.SupplierMapper;

@Validated
@Tag(name = "Suppliers")
@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierMapper supplierMapper;

    @Operation(summary = "Create supplier")
    @PostMapping
    public ResponseEntity<SupplierDto> create(
            @Parameter(required = true) @Valid @RequestBody CreateSupplierDto createSupplierDto) {
        Supplier supplier = supplierMapper.toEntity(createSupplierDto);
        
        Supplier created = supplierService.create(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierMapper.toDto(created));
    }

    @Operation(summary = "Get supplier by id")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getById(
            @Parameter(required = true) @PathVariable Long id) {
        return supplierService.findById(id)
                .map(supplierMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all suppliers")
    @GetMapping
    public List<SupplierDto> getAll() {
        return supplierService.findAll().stream().map(supplierMapper::toDto).collect(Collectors.toList());
    }
}
