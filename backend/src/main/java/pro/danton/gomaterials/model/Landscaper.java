package pro.danton.gomaterials.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "landscaper", uniqueConstraints = @UniqueConstraint(name = "uk_landscaper__name", columnNames = "name"))
public class Landscaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "landscaper")
    private List<BidRequest> bidRequests;
}