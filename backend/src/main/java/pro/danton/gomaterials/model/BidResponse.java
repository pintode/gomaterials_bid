package pro.danton.gomaterials.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bid_response")
public class BidResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price", precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "notes", length = 2048)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "supplier_id", foreignKey = @ForeignKey(name = "fk_bid_response__supplier_id"))
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "bid_request_id", foreignKey = @ForeignKey(name = "fk_bid_response__bid_request_id"))
    private BidRequest bidRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BidResponseStatus status;
}
