package pro.danton.gomaterials.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bid_request")
public class BidRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;

    @Column(name = "required_by", nullable = false)
    private LocalDate requiredBy;

    @ManyToOne
    @JoinColumn(name = "landscaper_id", foreignKey = @ForeignKey(name = "fk_bid_request__landscaper_id"))
    private Landscaper landscaper;

    @OneToMany(mappedBy = "bidRequest", cascade = CascadeType.ALL)
    private List<PlantItem> plantItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BidRequestStatus status;

    @OneToMany(mappedBy = "bidRequest")
    private List<BidResponse> bidResponses;
}
