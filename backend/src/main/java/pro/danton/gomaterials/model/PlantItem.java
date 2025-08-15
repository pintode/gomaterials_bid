package pro.danton.gomaterials.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plant_item", indexes = {
        @Index(name = "idx_plant_item__bid_request_id", columnList = "bid_request_id"),
})
public class PlantItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "grade", length = 100, nullable = false)
    private String grade;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "unit", length = 50, nullable = false)
    private String unit;

    @ManyToOne
    @JoinColumn(name = "bid_request_id", foreignKey = @ForeignKey(name = "fk_plant_item__bid_request_id"))
    private BidRequest bidRequest;
}
