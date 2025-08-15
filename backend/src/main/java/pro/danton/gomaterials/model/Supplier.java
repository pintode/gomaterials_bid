package pro.danton.gomaterials.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "supplier", uniqueConstraints = @UniqueConstraint(name = "uk_supplier__name", columnNames = "name"))
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "supplier")
    private List<BidResponse> bidResponses;
}
